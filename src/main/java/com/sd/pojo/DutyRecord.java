package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 值班记录表
 * @author Chen Hualiang
 * @create 2020-10-15 16:01
 */
@TableName("Duty_Record")
@Data
public class DutyRecord implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5514654604003148857L;

    @TableId(type = IdType.AUTO)
    private Integer id;

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
     *备注
     */
    @TableField("comments")
    private String comments;
    
    /**
     *交接班标志
     */
    @TableField("duty")
    private String duty;

    /**
     *最后更新人员
     */
    @TableField("updateUser")
    private String updateUser;
    
    /**
     *最后更新日期
     */
    @TableField(value = "updateDate",fill = FieldFill.INSERT_UPDATE)
//    @TableField("updateDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;


}
