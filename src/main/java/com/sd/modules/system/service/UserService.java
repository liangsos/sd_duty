package com.sd.modules.system.service;

import com.sd.modules.system.entity.User;
import com.sd.pojo.DutyRole;

import java.util.List;

/**
 * 用户信息服务类
 * @author Chen Hualiang
 * @create 2020-10-10 15:28
 */
public interface UserService {

    User findUserByUserName(String name);

    User findUserById(Integer id);

    //更新用户信息
    User updateUser(User user);

    List<User> getUserByType(String type,String addvcd);

    List<User> getDutyUser();

    int getDutyUserCount();

    boolean isExistUser(String userName);

    long saveDutyUser(User user);

    boolean delUser(String userid);

    int getDutyUserSort(String userType);

    User getUserByName(String name,String addvcd);

    long saveDutyRole(DutyRole dutyRole);

//    int userCount(String param);
//
//    User saveUser(User user);
//

//
//    //删除单条记录
//    void deleteUser(User user);

//    int insertDutyRole(DutyRole dutyRole);
}
