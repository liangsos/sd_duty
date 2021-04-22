package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyMat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-11-10 17:23
 */
public interface DutyMatMapper extends BaseMapper<DutyMat> {

    @Select({"<script> " +
            " select id,fileName,source,content,updateDate from " +
            " (select id,fileName,source,content,updateDate,ROW_NUMBER() over (order by updateDate desc) AS RowNumber from Duty_Mat where 1=1 " +
            " and addvcd = #{addvcd} " +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;' > and updateDate between #{beginTime} and #{endTime} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and updateDate &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and updateDate &lt;= #{endTime} </if> " +
            " ) Duty_Mat_Page WHERE RowNumber &gt; #{current} AND RowNumber &lt;= #{size} order by updateDate desc " +
            " </script>"})
    List<DutyMat> getDutyMat(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("current") int current,@Param("size") int size,@Param("addvcd") String addvcd);

    @Select({"<script> " +
            " select count(1) as total from Duty_Mat where 1=1 " +
            " and addvcd = #{addvcd}" +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;'> and updateDate between #{beginTime} and #{endTime} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and updateDate &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and updateDate &lt;= #{endTime} </if> " +
            "</script>"})
    int getDutyMatCount(@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("addvcd") String addvcd);
}
