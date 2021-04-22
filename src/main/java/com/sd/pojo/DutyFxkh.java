package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 值班-防汛抗旱工作表
 * @author Chen Hualiang
 * @create 2020-10-16 11:01
 */
@TableName("Duty_Fxkh")
@Data
public class DutyFxkh implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8860046278839955695L;

    @TableId("id")
    private Integer id;

    @TableField("time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    /**
     * 水情信息
     */
    @TableField("waterInfo")
    private String waterInfo;

    /**
     * 主要站点水情
     */
    @TableField("waterMain")
    private String waterMain;

    /**
     * 雨情信息
     */
    @TableField("rainInfo")
    private String rainInfo;

    /**
     * 主要雨情信息
     */
    @TableField("rainMain")
    private String rainMain;

    /**
     * 会商标题
     */
    @TableField("consultTitle")
    private String consultTitle;

    /**
     * 会商内容
     */
    @TableField("consult")
    private String consult;

    /**
     * 传真概述
     */
    @TableField("fax")
    private String fax;

    /**
     * 其他
     */
    @TableField("comments")
    private String comments;

    /**
     * 工情灾情
     */
    @TableField("disaster")
    private String disaster;

    /**
     * 防汛抗旱行动
     */
    @TableField("action")
    private String action;

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

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;
}
