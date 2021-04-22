package com.sd.modules.system.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.modules.system.vo.DutyRecordTjVO;
import com.sd.pojo.DutyRecord;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2020-10-15 16:10
 */
public interface DutyRecordMapper extends BaseMapper<DutyRecord> {


    @Select("select id,time,leaderComm,leader,member,comments,duty from "
            + "(select id,time,leaderComm,leader,member,comments,duty,ROW_NUMBER() over (order by time) AS RowNumber "
            + "from Duty_Record where addvcd = #{addvcd} and time between #{beginTime} and #{endTime} ) Duty_Bb_Page "
            + " WHERE RowNumber > #{star} AND RowNumber <= #{end} order by time")
    List<DutyRecord> getDutyBb(@Param("beginTime") String beginTime, @Param("endTime") String endTime,
            @Param("star") int star, @Param("end") int end,@Param("addvcd") String addvcd);

    @Select("SELECT t1.id id, t1.duty duty, t1.time start, (SELECT COUNT(*) FROM Duty_Record_Tel t2 WHERE t1.time=t2.time) AS tel FROM Duty_Record t1 WHERE t1.time >= #{beginTime} AND t1.time < #{endTime} " +
            "AND t1.addvcd = #{addvcd}")
    List<DutyRecordTjVO> getDutyRecord(Date beginTime, Date endTime,String addvcd);

    @Select("select * from (select top 99.999999 PERCENT u.id as id,u.name as name,'3' as type from Admin_User u inner join Duty_Role r ON u.id = r.user_id where r.user_type like '%3%' and u.addvcd = #{addvcd} and u.user_duty = 1) a " +
            "union all " +
            "select * from (select top 99.999999 PERCENT u.id as id,u.name as name,'2' as type from Admin_User u inner join Duty_Role r ON u.id = r.user_id where r.user_type like '%2%' and u.addvcd = #{addvcd} and u.user_duty = 1) b " +
            "union all " +
            "select * from (select top 99.999999 PERCENT u.id as id,u.name as name,'1' as type from Admin_User u inner join Duty_Role r ON u.id = r.user_id where r.user_type like '%1%' and u.addvcd = #{addvcd} and u.user_duty = 1) c")
    List<Map<String, Object>> getDutyTj(@Param("addvcd") String addvcd);
}
