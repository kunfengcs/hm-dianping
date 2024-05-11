package com.ruoyi.dianping_admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商户信息对象 tb_shop
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbShop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 商铺名称 */
    @Excel(name = "商铺名称")
    private String name;

    /** 商铺类型的id */
    @Excel(name = "商铺类型的id")
    private Long typeId;

    /** 商铺图片，多个图片以','隔开 */
    @Excel(name = "商铺图片，多个图片以','隔开")
    private String images;

    /** 商圈，例如陆家嘴 */
    @Excel(name = "商圈，例如陆家嘴")
    private String area;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 经度 */
    private String x;

    /** 维度 */
    private String y;

    /** 均价，取整数 */
    @Excel(name = "均价，取整数")
    private Integer avgPrice;

    /** 销量 */
    @Excel(name = "销量")
    private Integer sold;

    /** 评论数量 */
    @Excel(name = "评论数量")
    private Integer comments;

    /** 评分，1~5分，乘10保存，避免小数 */
    @Excel(name = "评分，1~5分，乘10保存，避免小数")
    private Integer score;

    /** 营业时间，例如 10:00-22:00 */
    @Excel(name = "营业时间，例如 10:00-22:00")
    private String openHours;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setTypeId(Long typeId) 
    {
        this.typeId = typeId;
    }

    public Long getTypeId() 
    {
        return typeId;
    }
    public void setImages(String images) 
    {
        this.images = images;
    }

    public String getImages() 
    {
        return images;
    }
    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setX(String x) 
    {
        this.x = x;
    }

    public String getX() 
    {
        return x;
    }
    public void setY(String y) 
    {
        this.y = y;
    }

    public String getY() 
    {
        return y;
    }
    public void setAvgPrice(Integer avgPrice) 
    {
        this.avgPrice = avgPrice;
    }

    public Integer getAvgPrice() 
    {
        return avgPrice;
    }
    public void setSold(Integer sold) 
    {
        this.sold = sold;
    }

    public Integer getSold() 
    {
        return sold;
    }
    public void setComments(Integer comments) 
    {
        this.comments = comments;
    }

    public Integer getComments() 
    {
        return comments;
    }
    public void setScore(Integer score) 
    {
        this.score = score;
    }

    public Integer getScore() 
    {
        return score;
    }
    public void setOpenHours(String openHours) 
    {
        this.openHours = openHours;
    }

    public String getOpenHours() 
    {
        return openHours;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("typeId", getTypeId())
            .append("images", getImages())
            .append("area", getArea())
            .append("address", getAddress())
            .append("x", getX())
            .append("y", getY())
            .append("avgPrice", getAvgPrice())
            .append("sold", getSold())
            .append("comments", getComments())
            .append("score", getScore())
            .append("openHours", getOpenHours())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
