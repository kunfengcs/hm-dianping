package com.ruoyi.dianping_admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 秒杀优惠券，与优惠券是一对一关系对象 tb_seckill_voucher
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbSeckillVoucher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联的优惠券的id */
    private Long voucherId;

    /** 库存 */
    @Excel(name = "库存")
    private Integer stock;

    /** 生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /** 失效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "失效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    public void setVoucherId(Long voucherId) 
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() 
    {
        return voucherId;
    }
    public void setStock(Integer stock) 
    {
        this.stock = stock;
    }

    public Integer getStock() 
    {
        return stock;
    }
    public void setBeginTime(Date beginTime) 
    {
        this.beginTime = beginTime;
    }

    public Date getBeginTime() 
    {
        return beginTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("voucherId", getVoucherId())
            .append("stock", getStock())
            .append("createTime", getCreateTime())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
