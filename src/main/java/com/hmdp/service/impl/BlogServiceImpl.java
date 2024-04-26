package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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

//        Optional<UserDTO> optionalUser = Optional.ofNullable(UserHolder.getUser());
//        Long userId = optionalUser.map(UserDTO::getId).orElse(null);

        //1,获取用户
        Long userId = UserHolder.getUser().getId();
        // 2,判断当前登录用户是否点赞
        String key = "blog:liked" + blog.getId();
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        blog.setIsLike(BooleanUtil.isTrue(isMember));
    }

    @Override
    public Result likeBlog(Long id) {
        // 1,获取登录用户
        Long userId = UserHolder.getUser().getId();
        // 2,判断当前登录用户是否点赞
        String key = "blog:liked" + id;
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        if (BooleanUtil.isFalse(isMember)) {
            // 3,1 如果未点赞，可以点赞
            // 3,2 数据库点赞数 + 1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            // 3.2 保存用户到redis 的set 集合中
            if (isSuccess){
                stringRedisTemplate.opsForSet().add(key,userId.toString());
            }
        }else {
            // 4.1 如果以点赞，取消点赞
            // 4.2 数据库点赞书 -1
            boolean isSuccess = update().setSql("liked = liked -1").eq("id", id).update();
            // 4.3 把用户从 Redis 的set集合中移除
            if (isSuccess) {
                stringRedisTemplate.opsForSet().remove(key,userId.toString());
            }
        }
        return Result.ok();
    }

    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }
}
