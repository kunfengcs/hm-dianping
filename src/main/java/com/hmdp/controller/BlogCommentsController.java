package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.hmdp.entity.BlogComments;
import com.hmdp.entity.User;
import com.hmdp.service.IBlogCommentsService;
import com.hmdp.service.IBlogService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Api(tags = "博客评论管理")
@RestController
@RequestMapping("/comment")
public class BlogCommentsController {
    @Resource
    private IBlogCommentsService commentService;

    @Resource
    private IBlogService blogService;
    @Resource
    private IUserService userService;

    /**
     * 新增或更新评论
     *
     * @param comment 评论内容
     * @return 操作结果
     */
    @ApiOperation(value = "新增或更新评论", notes = "根据评论ID判断是新增还是更新操作")
    @ApiResponses({
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result save(@RequestBody BlogComments comment) {
        if (comment.getId() == null) {
            Long id = UserHolder.getUser().getId();
            comment.setUserId(id);
            User user = userService.getById(id);
            comment.setNickName(user.getNickName());
            comment.setIcon(user.getIcon());

            if (comment.getAnswerId() != null) {    //判断回复则进行处理
                Long aid = comment.getAnswerId();
                BlogComments pComment = commentService.getById(aid);
                if (pComment.getParentId() != null && pComment.getParentId() != 0L) {//祖宗id,如果当前回复的父级有祖宗，那么就设置相同的祖宗
                    comment.setParentId(pComment.getParentId());
                }
            } else {
                comment.setParentId(0L);
            }
        }
        Long blogId = comment.getBlogId();
        Blog blog = blogService.query().eq("id", blogId).one();
        blog.setIsRead(false);
        boolean saveOrUpdate = blogService.saveOrUpdate(blog);
        boolean update = commentService.saveOrUpdate(comment);
        if (update && saveOrUpdate) {
            boolean id = blogService.update().setSql("comments=comments+1")
                    .eq("id", comment.getBlogId()).update();
            if (!id) {
                return Result.fail("出现错误");
            }
        }

        return Result.ok();
    }

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除评论", notes = "根据评论ID删除评论")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 404, message = "评论未找到"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        commentService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "批量删除评论", notes = "根据评论ID列表批量删除评论")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        commentService.removeByIds(ids);
        return Result.ok();
    }

    /**
     * 查询所有评论
     *
     * @return 所有评论列表
     */
    @ApiOperation(value = "查询所有评论", notes = "获取系统中所有评论信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
    public Result findAll() {
        return Result.ok(commentService.list());
    }

    /**
     * 根据博客ID获取评论树结构
     *
     * @param blogId 博客ID
     * @return 评论树结构
     */
    @ApiOperation(value = "根据博客ID获取评论树结构", notes = "返回包含回复的评论树形结构数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 404, message = "博客未找到"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/tree/{blogId}")
    public Result findTree(@PathVariable Long blogId) {  //查询所有的评论数据
//        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("article_id",articleId);
        List<BlogComments> articleComment = commentService.findCommentDetail(blogId);
        //查询评论数据不包括回复
        List<BlogComments> originList = articleComment.stream().filter(comment -> comment.getParentId() == 0).collect(Collectors.toList());
//            //设置评论数据的子节点，也就是回复内容
        for (BlogComments origin : originList) {
            //父子关系,表示回复的对象集合
            List<BlogComments> comments = articleComment.stream().filter(comment -> origin.getId().equals(comment.getParentId())).collect(Collectors.toList());
            comments.forEach(comment -> {
                Optional<BlogComments> pComment = articleComment.stream().filter(c1 -> c1.getId().equals(comment.getAnswerId())).findFirst();  // 找到当前评论的父级
                pComment.ifPresent((v -> {  // 找到父级评论的用户id和用户昵称，并设置给当前的回复对象
                    comment.setAUserID(v.getUserId());
                    comment.setANickname(v.getNickName());
                }));
            });
            origin.setChildren(comments);
        }
//            return Result.ok(originList);
        return Result.ok(originList);
    }

    /**
     * 根据ID查询单个评论
     *
     * @param id 评论ID
     * @return 单个评论详情
     */
    @ApiOperation(value = "根据ID查询单个评论", notes = "根据评论ID获取评论详细信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 404, message = "评论未找到"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Long id) {
        return Result.ok(commentService.getById(id));
    }

}
