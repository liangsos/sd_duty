package com.sd.modules.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyRecordTel;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Chen Hualiang
 * @create 2020-10-16 11:35
 */
public interface DutyRecordTelMapper extends BaseMapper<DutyRecordTel> {

    @Select("select a.*,b.name as realName from Duty_Record_Tel a left join Admin_User b on a.updateUser=b.user_name where a.time = " +
            "#{date} and a.addvcd = #{addvcd} order by a.id")
    List<DutyRecordTel> getDutyRecordTel(@Param("date") String date,@Param("addvcd") String addvcd);
}
