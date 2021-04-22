package com.sd.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 会商记录表
 * @author Chen Hualiang
 * @create 2020-10-16 14:48
 */
@TableName("Duty_Consult")
@Data
public class DutyConsult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3193554206472200896L;

    @TableId("id")
    private Integer id;

    @TableField("beginTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @TableField("endTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 会议名称
     */
    @TableField("name")
    private String name;

    /**
     * 会议地点
     */
    @TableField("place")
    private String place;

    /**
     * 主持人
     */
    @TableField("host")
    private String host;

    /**
     * 参加人员
     */
    @TableField("attendees")
    private String attendees;

    /**
     * 参加人数
     */
    @TableField("attend")
    private String attend;

    /**
     * 会商内容
     */
    @TableField("content")
    private String content;

    /**
     * 会商结论
     */
    @TableField("conslusion")
    private String conslusion;

    /**
     * 会商文档
     */
    @TableField("docment")
    private String docment;

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

    @TableField(exist = false)
    private String realName;

    @TableField(exist = false)
    private MultipartFile file;

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;

}
