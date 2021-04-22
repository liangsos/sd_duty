package com.sd.modules.system.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *菜单实体类
 * @author Chen Hualiang
 * @create 2020-10-12 9:38
 */
@Data
@TableName("sys_menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

//    private String icon;

    /**
     * 链接地址
     */
    @TableField()
    private String href;

    /**
     * 打开方式
     */
    @TableField()
    private String target;

    /**
     * 是否显示
     */
    @TableField()
    private Boolean isShow;

    /**
     * 类型（0表示菜单，1表示按钮，-1表示目录）
     */
    @TableField("is_menu")
    private Integer isMenu;

    @TableField("bg_color")
    private String bgColor;

    /**
     * 权限标识
     */
    @TableField()
    private String permission;

    @TableField(exist = false)
    private Integer dataCount;
}
