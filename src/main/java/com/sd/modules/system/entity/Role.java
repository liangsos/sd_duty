package com.sd.modules.system.entity;

import java.io.Serializable;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 角色实体类
 * @author Chen Hualiang
 * @create 2020-10-12 9:35
 */
@Data
@TableName("sys_role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String name;

    @TableField(exist = false)
    private Set<Menu> menuSet;

    @TableField(exist = false)
    private Set<User> userSet;
}
