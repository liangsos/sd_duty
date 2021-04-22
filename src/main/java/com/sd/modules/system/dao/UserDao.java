package com.sd.modules.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.modules.system.entity.User;

import java.util.Map;

/**
 * 用户信息 Mapper 接口
 * @author Chen Hualiang
 * @create 2020-10-12 9:20
 */
public interface UserDao extends BaseMapper<User> {
    User selectUserByMap(Map<String,Object> map);
}
