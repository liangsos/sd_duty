package com.sd.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.*;
//import com.google.common.collect.Sets;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Sets;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户实体类
 * @author Chen Hualiang
 * @create 2020-10-09 16:54
 */
@Data
@TableName("Admin_User")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id",type = IdType.AUTO)
    private Integer userid;

    /**
     *用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 真实姓名
     */
    @TableField("name")
    private String realName;

    /**
     *密码
     */
    @TableField("pass")
    private String password;

    /**
     *行政区号
     */
    @TableField("addvcd")
    private String addvcd;

    /**
     *手机
     */
    @TableField("user_tel")
    private String userTel;

    /**
     *部门
     */
    @TableField("user_depart")
    private String userDepart;

    /**
     *是否网站用户
     */
    @TableField("user_web")
    private int userWeb;

    /**
     *网站权限
     */
    @TableField("role_web")
    private int roleWeb;

    /**
     *后台管理权限
     */
    @TableField("role_admin")
    private int roleAdmin;

    /**
     *是否手机app用户
     */
    @TableField("user_phone")
    private int userPhone;

    /**
     *手机app是否审核
     */
    @TableField("user_phone_show")
    private int userPhoneShow;

    /**
     * 手机用户权限
     */
    @TableField("role_phone")
    private int rolePhone;

    /**
     *是否短信系统用户
     */
    @TableField("user_sms")
    private int userSms;

    /**
     *值班系统权限
     */
    @TableField("user_duty")
    private int userDuty;


    /**
     *备注
     */
    @TableField("mark")
    private String mark;

    /**
     *创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    

    /**
     * shiro加密盐
     */
//    private String salt;

    /**
     *电话
     */
//    @TableField("phone")
//    private String phone;

    /**
     *电子邮箱
     */
//    @TableField("email")
//    private String email;

    /**
     *角色
     */
    @TableField(exist=false)
    private String roleId;

    /**
     *人员类型
     */
    @TableField(exist=false)
    private String userType;

    /**
     *排序
     */
    @TableField(exist=false)
    private int sort;

    @TableField(exist=false)
    private Set<Role> roleLists = Sets.newHashSet();

    @TableField(exist=false)
    private Set<Menu> menus = Sets.newHashSet();

}
