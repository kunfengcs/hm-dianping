package com.ruoyi.dianping_admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 博客评论对象 tb_blog_comments
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbBlogComments extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 探店id */
    @Excel(name = "探店id")
    private Long blogId;

    /** 关联的1级评论id，如果是一级评论，则值为0 */
    @Excel(name = "关联的1级评论id，如果是一级评论，则值为0")
    private Long parentId;

    /** 回复的评论id */
    @Excel(name = "回复的评论id")
    private Long answerId;

    /** 回复的内容 */
    @Excel(name = "回复的内容")
    private String content;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Integer liked;

    /** 状态，0：正常，1：被举报，2：禁止查看 */
    @Excel(name = "状态，0：正常，1：被举报，2：禁止查看")
    private Integer status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setBlogId(Long blogId) 
    {
        this.blogId = blogId;
    }

    public Long getBlogId() 
    {
        return blogId;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setAnswerId(Long answerId) 
    {
        this.answerId = answerId;
    }

    public Long getAnswerId() 
    {
        return answerId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setLiked(Integer liked) 
    {
        this.liked = liked;
    }

    public Integer getLiked() 
    {
        return liked;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("blogId", getBlogId())
            .append("parentId", getParentId())
            .append("answerId", getAnswerId())
            .append("content", getContent())
            .append("liked", getLiked())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
