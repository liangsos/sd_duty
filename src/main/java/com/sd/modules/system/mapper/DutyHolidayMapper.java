package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyHoliday;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 18:00
 */
public interface DutyHolidayMapper extends BaseMapper<DutyHoliday> {
    @Select({"<script> " +
            "select id,time,type,comments from " +
            "(select id,time,(select daxx from Duty_Dict where type='0101' and dabh=Duty_Holiday.type) as type,comments,ROW_NUMBER() over (order by year(time) desc,time) AS RowNumber from Duty_Holiday where 1=1 " +
            "<if test='searchYear != &quot;&quot; and searchYear != null '>  and DATEPART(YEAR,time) = #{searchYear} " +
            "</if> ) Duty_Holiday_Page WHERE RowNumber &gt; #{start} AND RowNumber &lt;= #{end} order by year(time) desc,time" +
            "</script>"})
    List<DutyHoliday> getDutyHoliday(@Param("searchYear") String searchYear,@Param("start") int start,@Param("end") int end);

    @Select({"<script> " +
            "select count(1) as total from Duty_Holiday where 1=1 <if test='searchYear != &quot;&quot; and searchYear != null '>" +
            " and DATEPART(YEAR,time) = #{searchYear}" +
            "</if></script>"})
    int getDutyHolidayCount(@Param("searchYear") String searchYear);
}
