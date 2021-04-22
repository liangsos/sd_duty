package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 排班表
 * @author Chen Hualiang
 * @create 2020-10-19 9:44
 */
@TableName("Duty_Record_Plan")
@Data
public class DutyRecordPlan implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6684734507463423413L;

    @TableId("id")
    private int id;

    @TableField("time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    /**
     *委领导带班
     */
    @TableField("leaderComm")
    private String leaderComm;

    /**
     *带班
     */
    @TableField("leader")
    private String leader;

    /**
     *值班人员
     */
    @TableField("member")
    private String member;

    /**
     *最后更新人员
     */
    @TableField("updateUser")
    private String updateUser;

    /**
     *最后更新日期
     */
    @TableField(value = "updateDate",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;
}
