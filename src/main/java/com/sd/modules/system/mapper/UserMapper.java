package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.modules.system.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Chen Hualiang
 * @create 2020-10-12 11:11
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("select MAX(sort) as sort from Duty_User where userType like '%${userType}%' ")
    int getDutyUserSor(@Param("userType") String userType);
}
