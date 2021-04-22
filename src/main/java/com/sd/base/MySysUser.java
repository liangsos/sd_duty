package com.sd.base;

import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;

/**
 * 用户信息
 * @author Chen Hualiang
 * @create 2020-10-10 11:10
 */
public class MySysUser {
    public static Integer id() {
        return ShiroUser().getId();
    }

    public static String userName(){
        return  ShiroUser().getuserName();
    }

    public static String realName(){
        return  ShiroUser().getrealName();
    }

    public static AuthRealm.ShiroUser ShiroUser() {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }
}
