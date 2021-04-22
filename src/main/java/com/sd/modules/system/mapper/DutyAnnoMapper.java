package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyAnno;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-11-17 18:39
 */
public interface DutyAnnoMapper extends BaseMapper<DutyAnno> {
    @Select({"<script> " +
            " select id,fileName,source,content,updateDate from " +
            " (select id,fileName,source,content,updateDate,ROW_NUMBER() over (order by updateDate desc) AS RowNumber from Duty_Anno where 1=1 " +
            " and addvcd = #{addvcd}" +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;' > and updateDate between #{beginTime} and #{endTime} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and updateDate &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and updateDate &lt;= #{endTime} </if> " +
            " ) Duty_Anno_Page WHERE RowNumber &gt; #{current} AND RowNumber &lt;= #{size} order by updateDate desc " +
            " </script>"})
    List<DutyAnno> getDutyAnno(String beginTime, String endTime, int current, int size, String addvcd);

    @Select({"<script> " +
            " select count(1) as total from Duty_Anno where 1=1 " +
            " and addvcd = #{addvcd}" +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;'> and updateDate between #{beginTime} and #{endTime} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and updateDate &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and updateDate &lt;= #{endTime} </if> " +
            "</script>"})
    int getDutyAnnoCount(String beginTime, String endTime,String addvcd);
}
