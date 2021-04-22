package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sd.pojo.DutyConsult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2020-10-16 15:02
 */
public interface DutyConsultMapper extends BaseMapper<DutyConsult> {
    @Select("select a.*,b.name as realName from Duty_Consult a left join Admin_User b on a.updateUser=b.user_name where CONVERT(varchar(10), a.beginTime, 23)= "
            + "#{date} and a.addvcd = #{addvcd} order by a.beginTime")
    List<Map<String, Object>> getDutyConsultByTime(@Param("date") String date, @Param("addvcd") String addvcd);

    @Select({"<script>",
            "SELECT t1.*, t2.realName realName FROM Duty_Consult t1, Admin_User t2 WHERE t1.updateUser=t2.user_name",
            "<if test='begin != null'> AND t1.beginTime &gt;=#{begin}</if>",
            "<if test='end != null'> AND t1.beginTime &lt;=#{end}</if>", " ORDER BY t1.beginTime DESC", "</script>"})
    IPage<DutyConsult> getDutyConsultPage(Page<DutyConsult> page, Date begin, Date end);

    @Select({"<script>" +
            " select * from (select *,(SELECT name FROM Admin_User WHERE user_name=Duty_Consult.updateUser) AS realName,ROW_NUMBER() over (order by beginTime desc) AS RowNumber from Duty_Consult where 1=1 " +
            "and addvcd = #{addvcd} " +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;' > and beginTime between #{beginTime} and #{endTime} </if>" +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and beginTime &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and beginTime &lt;= #{endTime} </if> " +
            " ) Duty_Consult_Page " +
            " WHERE RowNumber &gt; #{current}  AND RowNumber &lt;= #{size} order by beginTime desc " +
            "</script>"})
    List<DutyConsult> getDutyConsult(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("current") long current, @Param("size") long size, @Param("addvcd") String addvcd);

    @Select({"<script> " +
            " select count(1) as total from Duty_Consult where 1=1 " +
            " and addvcd = #{addvcd} " +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;'> and beginTime between #{beginTime} and #{endTime} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and beginTime &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and beginTime &lt;= #{endTime} </if> " +
            "</script>"})
    int getDutyConsultCount(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("addvcd") String addvcd);
}
