package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Chen Hualiang
 * @create 2020-12-18 16:51
 */
public interface DutyRoleMapper extends BaseMapper<DutyRole> {
    @Select("select MAX(sort) as sort from Duty_Role where addvcd = #{addvcd} and user_type like '%${userType}%'")
    int getDutyUserSort(@Param("userType") String userType,@Param("addvcd") String addvcd);
}
