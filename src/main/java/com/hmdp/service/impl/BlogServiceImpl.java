package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IFollowService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Resource
    private IUserService userService;

    @Resource
    private IFollowService followService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(blog -> {
            queryBlogUser(blog);
            isBlogLiked(blog);
        });
        return Result.ok(records);
    }

    @Override
    public Result querBlogById(Long id) {
        Blog blog = getById(id);
        //如果为空
        if (blog == null) {
            return Result.fail("笔记不存在");
        }
        //查询用户ID并添加到blog对象中
        queryBlogUser(blog);
        //blog 是否被点赞
        isBlogLiked(blog);
        return Result.ok(blog);
    }

    private void isBlogLiked(Blog blog) {

        Optional<UserDTO> optionalUser = Optional.ofNullable(UserHolder.getUser());
        if (!optionalUser.isPresent()) {
            return; // 或者 return null; 根据你的业务需求决定
        }
        Long userId = optionalUser.get().getId();

//        UserDTO user = UserHolder.getUser();
//        if (user == null) {
//            return;
//        }
        //1,获取用户
//        Long userId = user.getId();
        // 2,判断当前登录用户是否点赞
        String key = "blog:liked" + blog.getId();
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        blog.setIsLike(score != null);
    }

    @Override
    public Result likeBlog(Long id) {
        // 1,获取登录用户
        Long userId = UserHolder.getUser().getId();
        // 2,判断当前登录用户是否点赞
        String key = "blog:liked" + id;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if (score == null) {
            // 3,1 如果未点赞，可以点赞
            // 3,2 数据库点赞数 + 1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            // 3.2 保存用户到redis 的set 集合中
            if (isSuccess){
                stringRedisTemplate.opsForZSet().add(key,userId.toString(),System.currentTimeMillis());
            }
        }else {
            // 4.1 如果以点赞，取消点赞
            // 4.2 数据库点赞书 -1
            boolean isSuccess = update().setSql("liked = liked -1").eq("id", id).update();
            // 4.3 把用户从 Redis 的set集合中移除
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().remove(key,userId.toString());
            }
        }
        return Result.ok();
    }

    @Override
    public Result queryLikesById(Long id) {
        String key = "blog:liked" + id;
        // 1, 查询前5的点赞用户 zrange key 0 4
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        // 2, 解析出用户id,从 string 到 long
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String isStr = StrUtil.join(",", ids);
        // 3, 根据用户id查出用户 where id in (5,4,3,2,1) order by field(id,5,4,3,2,1)
        List<UserDTO> userDTOS = userService.query()
                .in("id", ids)
                .last("ORDER BY FIELD(id," + isStr + ")").list()
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))//使用UserDTO 去隐私数据
                .collect(Collectors.toList());
        // 4, 返回
        return Result.ok(userDTOS);
    }

    @Override
    public Result saveBlog(Blog blog) {
        // 1，获取登录用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        //2，保存探店博文
        boolean isSuccess = save(blog);
        if (!isSuccess) {
            return Result.fail("新增笔记失败");
        }
        // 3,feed流实现，
        // 3,查询笔记作者的所有粉丝 select * from tb_follow where follow_user_id = ?
        followService.query().eq("follow_user_id",user.getId()).list().forEach(follow ->{
            // 4, 推送笔记到每一个粉丝的收件箱
            String feedKey = "feed:" + follow.getUserId();
            stringRedisTemplate.opsForZSet().add(feedKey,blog.getId().toString(),System.currentTimeMillis());
        });
        //3，返回id
        return Result.ok(blog.getId());
    }

    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }
}
