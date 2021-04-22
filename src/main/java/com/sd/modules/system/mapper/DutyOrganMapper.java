package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyOrgan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 8:48
 */
public interface DutyOrganMapper extends BaseMapper<DutyOrgan> {

    @Select("select id,name,leader,comments from " +
            "(select id,name,leader,comments,ROW_NUMBER() over (order by id) AS RowNumber from Duty_Organ) Duty_Organ_Page " +
            "WHERE RowNumber > #{start} AND RowNumber <= #{end} order by id")
    List<DutyOrgan> getDutyOrgan(@Param("start") int start,@Param("end") int end);
}
