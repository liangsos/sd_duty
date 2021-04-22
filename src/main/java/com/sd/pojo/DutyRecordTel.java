package com.sd.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 值班-来电记录表
 * @author Chen Hualiang
 * @create 2020-10-16 11:30
 */
@TableName("Duty_Record_Tel")
@Data
public class DutyRecordTel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1566352369209247510L;

    private Integer id;

    @TableField("time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    /**
     * 电话类型
     */
    @TableField("type")
    private String type;

    /**
     * 来电单位
     */
    @TableField("units")
    private String units;

    /**
     * 来电号码
     */
    @TableField("telephone")
    private String telephone;

    /**
     * 来电内容
     */
    @TableField("content")
    private String content;

    /**
     * 录音文件名称
     */
    @TableField("audio")
    private String audio;

    /**
     *最后更新人员
     */
    @TableField("updateUser")
    private String updateUser;

    /**
     *最后更新日期
     */
    @TableField("updateDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    @TableField(exist = false)
    private String realName;

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;
}
