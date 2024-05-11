package com.ruoyi.dianping_admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户博客对象 tb_blog
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbBlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 商户id */
    @Excel(name = "商户id")
    private Long shopId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 探店的照片，最多9张，多张以","隔开 */
    @Excel(name = "探店的照片，最多9张，多张以隔开")
    private String images;

    /** 探店的文字描述 */
    @Excel(name = "探店的文字描述")
    private String content;

    /** 点赞数量 */
    @Excel(name = "点赞数量")
    private Integer liked;

    /** 评论数量 */
    @Excel(name = "评论数量")
    private Integer comments;

    /** 是否查看：0表示未读，1表示已读 */
    private Integer isRead;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setImages(String images) 
    {
        this.images = images;
    }

    public String getImages() 
    {
        return images;
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
    public void setComments(Integer comments) 
    {
        this.comments = comments;
    }

    public Integer getComments() 
    {
        return comments;
    }
    public void setIsRead(Integer isRead) 
    {
        this.isRead = isRead;
    }

    public Integer getIsRead() 
    {
        return isRead;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shopId", getShopId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("images", getImages())
            .append("content", getContent())
            .append("liked", getLiked())
            .append("comments", getComments())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isRead", getIsRead())
            .toString();
    }
}
