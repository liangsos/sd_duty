package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyRecordDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2020-10-20 10:48
 */
public interface DutyRecordDetailMapper extends BaseMapper<DutyRecordDetail> {
    @Select("select a.* from Duty_Record_Detail a,dbo.Duty_Record b where a.time=b.time and ISNULL(a.flag,'')='' and b.duty='1' and a.time < " +
            "#{time} and a.addvcd = #{addvcd} order by a.time")
    List<Map<String,Object>> selectDutyTj(@Param("time") String time,@Param("addvcd") String addvcd);
//    @Select({"<script>" +
//            "update Duty_Record_Detail set " +
//            "<if test='endTime == null'>" +
//            "endTime = #{endTime}, " +
//            "</if> " +
//            "hours = #{tempHour},days = #{days},flag = '1',where id = #{id} " +
//            "</script>"})
//    int updateRecordDetail(@Param("endTime") Date endTime, @Param("tempHour") double tempHour, @Param("days") double days, @Param("id") int id);
}
