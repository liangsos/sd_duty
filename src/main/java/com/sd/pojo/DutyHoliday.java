package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 17:37
 */
@TableName("Duty_Holiday")
@Data
public class DutyHoliday {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    @TableField("type")
    private String type;

    @TableField("comments")
    private String comments;

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

}
