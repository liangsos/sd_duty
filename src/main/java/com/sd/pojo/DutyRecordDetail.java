package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Chen Hualiang
 * @create 2020-10-20 10:11
 */
@TableName("Duty_Record_Detail")
@Data
public class DutyRecordDetail implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1827543693621174206L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    /**
     *值班人员
     */
    @TableField("member")
    private String member;

    /**
     * 开始时间
     */
    @TableField("beginTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 结束时间
     */
    @TableField("endTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 累计小时数
     */
    @TableField("hours")
    private BigDecimal hours;

    /**
     * 合计天数
     */
    @TableField("days")
    private BigDecimal days;
    
    /**
     *计算标志
     */

    private String flag;

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;
    
    
}
