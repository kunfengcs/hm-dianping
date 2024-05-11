package com.ruoyi.dianping_admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 优惠券对象 tb_voucher
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbVoucher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 商铺id */
    @Excel(name = "商铺id")
    private Long shopId;

    /** 代金券标题 */
    @Excel(name = "代金券标题")
    private String title;

    /** 副标题 */
    @Excel(name = "副标题")
    private String subTitle;

    /** 使用规则 */
    @Excel(name = "使用规则")
    private String rules;

    /** 支付金额，单位是分。例如200代表2元 */
    @Excel(name = "支付金额，单位是分。例如200代表2元")
    private Integer payValue;

    /** 抵扣金额，单位是分。例如200代表2元 */
    @Excel(name = "抵扣金额，单位是分。例如200代表2元")
    private Integer actualValue;

    /** 0,普通券；1,秒杀券 */
    @Excel(name = "0,普通券；1,秒杀券")
    private Integer type;

    /** 1,上架; 2,下架; 3,过期 */
    @Excel(name = "1,上架; 2,下架; 3,过期")
    private Integer status;

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
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setSubTitle(String subTitle) 
    {
        this.subTitle = subTitle;
    }

    public String getSubTitle() 
    {
        return subTitle;
    }
    public void setRules(String rules) 
    {
        this.rules = rules;
    }

    public String getRules() 
    {
        return rules;
    }
    public void setPayValue(Integer payValue) 
    {
        this.payValue = payValue;
    }

    public Integer getPayValue() 
    {
        return payValue;
    }
    public void setActualValue(Integer actualValue) 
    {
        this.actualValue = actualValue;
    }

    public Integer getActualValue() 
    {
        return actualValue;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
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
            .append("shopId", getShopId())
            .append("title", getTitle())
            .append("subTitle", getSubTitle())
            .append("rules", getRules())
            .append("payValue", getPayValue())
            .append("actualValue", getActualValue())
            .append("type", getType())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
