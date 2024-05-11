package com.ruoyi.dianping_admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户关注对象 tb_follow
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbFollow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 关联的用户id */
    @Excel(name = "关联的用户id")
    private Long followUserId;

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
    public void setFollowUserId(Long followUserId) 
    {
        this.followUserId = followUserId;
    }

    public Long getFollowUserId() 
    {
        return followUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("followUserId", getFollowUserId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
