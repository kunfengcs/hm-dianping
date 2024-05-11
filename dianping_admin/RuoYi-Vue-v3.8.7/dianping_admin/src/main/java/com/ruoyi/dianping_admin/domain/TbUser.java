package com.ruoyi.dianping_admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户对象 tb_user
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 密码，加密存储 */
    @Excel(name = "密码，加密存储")
    private String password;

    /** 昵称，默认是用户id */
    @Excel(name = "昵称，默认是用户id")
    private String nickName;

    /** 人物头像 */
    @Excel(name = "人物头像")
    private String icon;

    /** 是否是商户 1商户 0用户 */
    @Excel(name = "是否是商户 1商户 0用户")
    private Integer isShop;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }
    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setIsShop(Integer isShop) 
    {
        this.isShop = isShop;
    }

    public Integer getIsShop() 
    {
        return isShop;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("phone", getPhone())
            .append("password", getPassword())
            .append("nickName", getNickName())
            .append("icon", getIcon())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isShop", getIsShop())
            .toString();
    }
}
