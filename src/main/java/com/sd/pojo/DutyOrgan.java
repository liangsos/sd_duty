package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 主要机构名称
 * @author Chen Hualiang
 * @create 2020-10-27 8:39
 */
@TableName("Duty_Organ")
@Data
public class DutyOrgan implements Serializable {
    private static final long serialVersionUID = 8595584669474532053L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 机构名称
     */
    @TableField("name")
    private String name;

    /**
     * 主要领导
     */
    @TableField("leader")
    private String leader;

    /**
     * 备注
     */
    @TableField("comments")
    private String comments;

}
