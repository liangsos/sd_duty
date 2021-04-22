package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.entity.User;
import com.sd.modules.system.mapper.DutyRoleMapper;
import com.sd.modules.system.mapper.UserMapper;
import com.sd.modules.system.service.UserService;
import com.sd.pojo.DutyRole;
import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Chen Hualiang
 * @create 2020-10-12 9:14
 */
@Service("userService")
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DutyRoleMapper dutyRoleMapper;

    @Override
    public User findUserByUserName(String name) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select().eq("user_name",name);
        User user = userMapper.selectOne(qw);
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        User user = userMapper.selectById(id);
        DutyRole dutyRole = dutyRoleMapper.selectById(id);

        user.setRoleId(dutyRole.getRoleId());
        user.setUserType(dutyRole.getUserType());
        user.setSort(dutyRole.getSort());
        return user;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public User updateUser(User user) {
        userMapper.updateById(user);
        return user;
    }

    /**
     * 根据分类获取人员列表
     * @param type
     * @return
     */
    @Override
    public List<User> getUserByType(String type,String addvcd) {
        QueryWrapper<DutyRole> qw = new QueryWrapper<>();
        qw.select().eq("addvcd",addvcd).like("user_type",type).orderByAsc("user_type","sort");
        List<DutyRole> list = dutyRoleMapper.selectList(qw);
        List<Integer> ids = list.stream().map(DutyRole::getUserId).collect(Collectors.toList());

        if (ids.size()>0){//存在对应人员类型的用户
            QueryWrapper<User> userQw = new QueryWrapper<>();
            userQw.select().in("id",ids);
            List<User> users = userMapper.selectList(userQw);
            return users;
        }else {//不存在对应人员类型的用户
            return null;
        }

//        QueryWrapper<User> userQw = new QueryWrapper<>();
//        userQw.select().in(ids.size()>0,"id",ids);
//        List<User> users = userMapper.selectList(userQw);

//        qw.select("realName").like("userType",type).orderByAsc("userType","sort");
//        List<User> list = userMapper.selectList(qw);
//        return users;
    }

    /**
     * 值班人员列表
     * @return
     */
    @Override
    public List<User> getDutyUser() {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select().eq("addvcd",user.getAddvcd()).eq("user_duty","1");
        List<User> list = userMapper.selectList(qw);
        return list;
    }

    /**
     * 值班人员总数
     * @return
     */
    @Override
    public int getDutyUserCount() {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select().eq("addvcd",user.getAddvcd()).eq("user_duty","1");
        int i = userMapper.selectCount(qw);
        return i;
    }

    @Override
    public boolean isExistUser(String userName) {
        boolean res = false;
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select().eq("user_name",userName);
        User user = userMapper.selectOne(qw);
        if (user != null){
            res = true;
        }
        return res;
    }

    /**
     * 新建、修改用户
     * @param user
     * @return
     */
    @Override
    public long saveDutyUser(User user) {
        long res = 0;
        Integer id = user.getUserid();
        if (id == null){//新增
            int insert = userMapper.insert(user);
//            dutyRoleMapper.insert(dutyRole);
            if (insert > 0){
                res = user.getUserid();
            }
        }else {//修改
            int update = userMapper.updateById(user);
//            dutyRoleMapper.updateById(dutyRole);
            if (update > 0){
                res = user.getUserid();
            }
        }
        return res;
    }

    @Override
    public boolean delUser(String userid) {
        boolean flag = false;
        int id = Integer.parseInt(userid);
        User delUser = findUserById(id);
        delUser.setUserDuty(0);
        int res = userMapper.updateById(delUser);
//        int res = userMapper.deleteById(userid);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public int getDutyUserSort(String userType) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        if (userType.contains("1")){
            userType = "1";
        }
        if (userType.contains("2")){
            userType = "2";
        }else {
            userType = "3";
        }
        int sort = dutyRoleMapper.getDutyUserSort(userType,addvcd);
        return sort;
    }

    @Override
    public User getUserByName(String name,String addvcd) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("name",name).eq("addvcd",addvcd);
        User user = userMapper.selectOne(qw);
        return user;
    }

    @Override
    public long saveDutyRole(DutyRole dutyRole) {
        long res = 0;
        int insert = dutyRoleMapper.insert(dutyRole);
        if (insert > 0){
            res = dutyRole.getUserId();
        }
        return res;
    }

}
