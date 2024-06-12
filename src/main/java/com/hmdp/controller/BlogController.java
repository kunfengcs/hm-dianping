package com.hmdp.controller;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.service.IBlogService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import io.swagger.annotations.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Api(tags = "博客模块")
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;
    @Resource
    private IUserService userService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @PostMapping
    @ApiOperation(value = "保存博客", notes = "用户发布新的博客文章")
    @ApiResponses({
            @ApiResponse(code = 200, message = "保存成功"),
            @ApiResponse(code = 401, message = "未认证"),
            @ApiResponse(code = 500, message = "服务器错误")
    })
    public Result saveBlog(@RequestBody Blog blog) {
        UserDTO user = UserHolder.getUser();
        Page<Blog> page = blogService.query().eq("user_id", user.getId())
                .page(new Page<>(1, SystemConstants.MAX_PAGE_SIZE));
        long total = page.getTotal();
        int pageSize = SystemConstants.MAX_PAGE_SIZE;
        int totalPages = (int) ((total + pageSize - 1) / pageSize);
        String key ="";
        for (int i = 1; i <= totalPages; i++) {

            key = "blog:of:"+user.getId()+"_"+i;
            stringRedisTemplate.delete(key);
        }

       return blogService.saveBlog(blog);
    }

    @PutMapping("/like/{id}")
    @ApiOperation(value = "点赞博客", notes = "用户对指定博客进行点赞操作")
    @ApiResponses({
            @ApiResponse(code = 200, message = "点赞成功"),
            @ApiResponse(code = 404, message = "博客不存在"),
            @ApiResponse(code = 500, message = "服务器错误")
    })
    public Result likeBlog(@PathVariable("id") Long id) {
//        // 修改点赞数量
//        blogService.update()
//                .setSql("liked = liked + 1").eq("id", id).update();
        return blogService.likeBlog(id);
    }

    @ApiOperation(value = "刷新缓存", notes = "刷新缓存")
    @GetMapping("/cache")
    public Result queryCache(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        UserDTO user = UserHolder.getUser();
        String key = "blog:of:"+user.getId()+"_"+current;
        stringRedisTemplate.delete(key);
        return Result.ok();
    }

//    @GetMapping("/of/me")
//    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
//        // 获取登录用户
//        UserDTO user = UserHolder.getUser();
//        //从缓存中存数据
//        String key = "blog:of:"+user.getId()+"_"+current;
//
//        String cachedData = stringRedisTemplate.opsForValue().get(key);
//
//        if (cachedData != null) {
//            // Data found in cache, return it
//            List<Blog> records = JSONUtil.toList(JSONUtil.parseArray(cachedData), Blog.class);
//            return Result.ok(records);
//        } else {
//            // Data not found in cache, query database
//            Page<Blog> page = blogService.query().eq("user_id", user.getId())
//                    .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
//            List<Blog> records = page.getRecords();
//            long pageTotal = page.getTotal();
//            // Store the retrieved data in Redis cache
//            Map<String, Object> resultData = new HashMap<>();
//            resultData.put("records", records);
//            resultData.put("totalPage", pageTotal);
//            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(resultData));
//            return Result.ok(resultData);
//        }
//        // 根据用户查询
////        Page<Blog> page = blogService.query()
////                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
////        // 获取当前页数据
////        List<Blog> records = page.getRecords();
////        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(records));
////        return Result.ok(records);
//    }

    @ApiOperation(value = "查询我的博客", notes = "查询我的博客")
    @GetMapping("/of/me")
    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();

        // 从缓存中存数据
        String key = "blog:of:" + user.getId() + "_" + current;
        String cachedData = stringRedisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            // 缓存中存在数据，则将缓存的数据转换为Map类型并返回
            // 初始化一个ObjectMapper对象
            ObjectMapper objectMapper = new ObjectMapper();

            // 定义一个TypeReference对象，用于指定转换后的类型
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
            // 将String类型的缓存数据转换为Map类型
            try {
                // 通过readValue方法将缓存数据转换为Map类型
                Map<String, Object> resultData = objectMapper.readValue(cachedData, typeRef);
                return Result.ok(resultData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        } else {
            // 缓存中不存在数据，则从数据库中查询数据
            Page<Blog> page = blogService.query().eq("user_id", user.getId())
                    .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        List<Blog> records = page.getRecords();
            long pageTotal = page.getPages();
            if (pageTotal == 0){
                return Result.ok();
            }
            // 将查询到的数据存入Map中
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("records", records);
            resultData.put("totalPage", pageTotal);

            // 将数据转换为JSON字符串，并存入缓存
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(resultData));

            return Result.ok(resultData);
        }
    }

    @ApiOperation(value = "查询热门博客", notes = "获取热门博客")
    @GetMapping("/hot")
    public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return blogService.queryHotBlog(current);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询博客", notes = "根据博客ID获取博客详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 404, message = "博客不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result queryBlogById(@PathVariable("id") Long id) {
        return blogService.queryBlogById(id);
    }

    /**
     * 查询博客点赞数
     *
     * @param id 博客ID
     * @return 点赞数
     */
    @ApiOperation(value = "查询博客点赞数", notes = "获取指定博客的点赞总数")
    @GetMapping("/likes/{id}")
    public Result queryLikesById(@PathVariable("id") Long id) {
        return blogService.queryLikesById(id);
    }

    /**
     * 点击他人主页时查询所有的博客
     * @param current
     * @param id
     * @return
     */
    @ApiOperation(value = "根据用户ID查询博客", notes = "查询指定用户的博客列表")
    @GetMapping("/of/user")
    public Result queryBlogByUserId(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", id)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        //获取当前页数据
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }

    /**
     * 添加到已读消息列表
     *
     * @param current 当前页码
     * @return 消息列表及总页数
     */
    @ApiOperation(value = "添加到已读消息列表", notes = "查询用户未读的博客，并将其标记为已读")
    @PostMapping("/queryMe/read")
    public Result addToMessageList(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        UserDTO user = UserHolder.getUser();
        Long id = user.getId();
        Page<Blog> page = blogService.query().eq("user_id", id).
                eq("isRead", false).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("records", page.getRecords());
        resultData.put("totalPage", page.getPages());
        return Result.ok(resultData);
    }
    /**
     * 查询用户关注人最新博客列表
     *
     * @param max     上一次分页查询，最后一个博客的时间戳
     * @param offset  偏移量 来自同一时间戳导致的错误，是最后一个博客时间戳相同的数量
     * @return 博客列表
     */
    @ApiOperation(value = "查询关注人的最新博客", notes = "获取关注人的最新博客列表")
    @GetMapping("/of/follow")
    public Result queryBlogOfFollow(
            @RequestParam("lastId") Long max, @RequestParam(value = "offset", defaultValue = "0") Integer offset
    ) {
        return blogService.queryBlogOfFollow(max, offset);
    }

    /**
     * 更新博客阅读状态
     *
     * @param blogId 博客ID
     * @return 更新后的博客详情
     */
    @ApiOperation(value = "更新博客阅读状态", notes = "将指定博客标记为已读")
    @PostMapping("/removeId")
    public Result queryBlogOfremoveId(
            @RequestParam("blogId") Long blogId){
        UserDTO user = UserHolder.getUser();
        Long userId = user.getId();
        Blog blog = blogService.query().eq("user_id", userId)
                .eq("id", blogId).one();
        blog.setIsRead(true);
        blogService.saveOrUpdate(blog);
        return Result.ok(blog);

    }

    @DeleteMapping("/del/{id}")
    @ApiOperation(value = "删除博客", notes = "根据博客ID删除博客")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 404, message = "未找到相应博客"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result delete(@PathVariable("id") @ApiParam("博客ID") Long id) {
        UserDTO user = UserHolder.getUser();
        Page<Blog> page = blogService.query().eq("user_id", user.getId())
                .page(new Page<>(1, SystemConstants.MAX_PAGE_SIZE));
        long total = page.getTotal();
        int pageSize = SystemConstants.MAX_PAGE_SIZE;
        int totalPages = (int) ((total + pageSize - 1) / pageSize);
        blogService.removeById(id);
        String key ="";
        for (int i = 1; i <= totalPages; i++) {
            key = "blog:of:"+user.getId()+"_"+i;
            stringRedisTemplate.delete(key);
        }
        return Result.ok();
    }
}
