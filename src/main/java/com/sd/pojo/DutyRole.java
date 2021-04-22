package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Chen Hualiang
 * @create 2020-12-18 16:41
 */
@TableName("Duty_Role")
@Data
public class DutyRole implements Serializable {

    private static final long serialVersionUID = -4944076919900336264L;

    @TableId(value = "user_id",type = IdType.INPUT)
    private int userId;

    @TableField("role_id")
    private String roleId;

    @TableField("user_type")
    private String userType;

    @TableField("sort")
    private int sort;

    @TableField("addvcd")
    private String addvcd;
}
