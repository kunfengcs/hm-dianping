package com.ruoyi.dianping_admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 签到对象 tb_sign
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public class TbSign extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 签到的年 */
    @Excel(name = "签到的年")
    private String year;

    /** 签到的月 */
    @Excel(name = "签到的月")
    private Integer month;

    /** 签到的日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签到的日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 是否补签 */
    @Excel(name = "是否补签")
    private Integer isBackup;

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
    public void setYear(String year) 
    {
        this.year = year;
    }

    public String getYear() 
    {
        return year;
    }
    public void setMonth(Integer month) 
    {
        this.month = month;
    }

    public Integer getMonth() 
    {
        return month;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setIsBackup(Integer isBackup) 
    {
        this.isBackup = isBackup;
    }

    public Integer getIsBackup() 
    {
        return isBackup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("year", getYear())
            .append("month", getMonth())
            .append("date", getDate())
            .append("isBackup", getIsBackup())
            .toString();
    }
}
