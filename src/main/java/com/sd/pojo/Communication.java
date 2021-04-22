package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Chen Hualiang
 * @create 2020-11-12 11:10
 */
@TableName("Communication")
@Data
public class Communication {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     *姓名
     */
    @TableField("name")
    private String name;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     *职务
     */
    @TableField("position")
    private String position;

    /**
     *电话
     */
    @TableField("tel")
    private String tel;

    /**
     *手机
     */
    @TableField("phone")
    private String phone;


    /**
     *值班电话
     */
    @TableField("dutyTel")
    private String dutyTel;

    /**
     *排序
     */
    @TableField("sort")
    private int sort;

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
