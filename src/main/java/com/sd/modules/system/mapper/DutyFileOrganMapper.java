package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyFileOrgan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DutyFileOrganMapper extends BaseMapper<DutyFileOrgan> {

    @Select("select * from (select *,ROW_NUMBER() over (order by convert(int,sort)) AS RowNumber from Duty_File_Organ) Duty_File_Organ_Page " +
            "WHERE RowNumber > #{start} AND RowNumber <= #{end} order by convert(int,sort)")
    List<DutyFileOrgan> getDutyFileOrgan(@Param("start") int start,@Param("end") int end);

    @Select("select MAX(sort) sort from Duty_File_Organ")
    int getSort();
}
