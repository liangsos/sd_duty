package com.sd.modules.system.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 值班日志实体类
 * @author Chen Hualiang
 * @create 2020-10-09 16:00
 */
@Data
@TableName("Duty_Log")
public class Log {
    private static final long serialVersionUID = 1L;

    /**
     * 操作表名
     */
    @TableField("tableName")
    private String tableName;

    /**
     * 所操作表记录主键ID
     */
    @TableField("tableId")
    private int tabaleId;

    /**
     *操作类型 1新增2删除3修改4查询
     */
    @TableField("type")
    private String type;

    /**
     *操作内容
     */
    @TableField("content")
    private String content;
    
    /**
     *操作人员
     */
    @TableField("updateUser")
    private String updateUser;

    /**
     *操作时间
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
