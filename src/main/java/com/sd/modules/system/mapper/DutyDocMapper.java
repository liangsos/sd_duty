package com.sd.modules.system.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sd.pojo.DutyConsult;
import com.sd.pojo.DutyDoc;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DutyDocMapper extends BaseMapper<DutyDoc> {

//    @Select({ "<script>", "SELECT * FROM Duty_Doc ", "<if test='begin != null'> AND updateDate &gt;=#{begin}</if>",
//            "<if test='end != null'> AND updateDate &lt;=#{end}</if>",
//            "<if test='type != null'> AND fileType = #{type}</if>", " ORDER BY updateDate DESC", "</script>" })
//    IPage<DutyDoc> getDutyDocPages(Page<DutyConsult> page, Date begin, Date end, String type);

    @Select({"<script> " +
            " select id,fileType,fileName,source,content,updateDate from " +
            " (select id,(select daxx  from Duty_Dict where type='0103' and dabh=Duty_Doc.fileType) as fileType,fileName,source,content,updateDate,ROW_NUMBER() over (order by updateDate desc) AS RowNumber from Duty_Doc where 1=1 " +
            " and addvcd = #{addvcd} " +
            " <if test='fileType != &quot;&quot;' > and fileType= #{fileType} </if>" +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;' > and updateDate between #{beginTime} and #{endTime} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and updateDate &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and updateDate &lt;= #{endTime} </if> " +
            " ) Duty_Doc_Page WHERE RowNumber &gt; #{current} AND RowNumber &lt;= #{size} order by updateDate desc " +
            " </script>"})
    List<DutyDoc> getDutyDoc(@Param("fileType") String fileType,@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("current") long current,@Param("size") long size,@Param("addvcd") String addvcd);

    @Select({"<script> " +
            " select count(1) as total from Duty_Doc where 1=1 " +
            " and addvcd = #{addvcd} " +
            " <if test='fileType != &quot;&quot;' > and fileType= #{fileType} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime != &quot;&quot;'> and updateDate between #{beginTime} and #{endTime} </if> " +
            " <if test='beginTime != &quot;&quot; and endTime == &quot;&quot;' > and updateDate &gt;= #{beginTime} </if> " +
            " <if test='beginTime == &quot;&quot; and endTime != &quot;&quot;' > and updateDate &lt;= #{endTime} </if> " +
            "</script>"})
    int getDutyDocCount(@Param("fileType") String fileType,@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("addvcd") String addvcd);
}
