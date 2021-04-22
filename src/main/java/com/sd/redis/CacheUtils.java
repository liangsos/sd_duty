package com.sd.redis;

import com.sd.base.MySysUser;
import com.sd.modules.system.entity.User;
import com.sd.modules.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

/**
 * 缓存工具类
 * @author Chen Hualiang
 * @create 2020-10-10 11:06
 */
public class CacheUtils {

    @Autowired
    private UserMapper userMapper;
    /**
     * 清除当前用户redis缓存
     */
    @Caching(evict = {
            @CacheEvict(value = "user", key = "'user_id_'+T(String).valueOf(#result.id)",condition = "#result.id != null and #result.id != 0"),
            @CacheEvict(value = "user", key = "'user_name_'+#result.userName", condition = "#result.userName !=null and #result.userName != ''"),
            @CacheEvict(value = "user", key = "'user_email_'+#result.email", condition = "#result.email != null and #result.email != ''"),
            @CacheEvict(value = "user", key = "'user_tel_'+#result.phone", condition = "#result.phone != null and #result.phone != ''" ),
    })
    public User clearUserCache(){
        User user = userMapper.selectById(MySysUser.id());
//        user.setId(MySysUser.id());
        return user;
    }
}
