package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Chen Hualiang
 * @create 2020-11-17 18:38
 */
@TableName("Duty_Anno")
@Data
public class DutyAnno implements Serializable {
    private static final long serialVersionUID = 1970634744602278598L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("fileName")
    private String fileName;

    @TableField("source")
    private String source;

    @TableField("content")
    private String content;

    @TableField("updateUser")
    private String updateUser;

    @TableField(value = "updateDate",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    @TableField(exist = false)
    private MultipartFile file;

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;
}
