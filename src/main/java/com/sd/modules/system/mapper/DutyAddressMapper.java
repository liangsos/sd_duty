package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sd.pojo.DutyAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-28 10:56
 */
public interface DutyAddressMapper extends BaseMapper<DutyAddress> {
    @Select("select id,address,comments from (select id,address,comments,ROW_NUMBER() over (order by id) AS RowNumber from Duty_Address WHERE addvcd = #{addvcd}) Duty_Address_Page " +
            "WHERE RowNumber > #{start} AND RowNumber <= #{end} order by id")
    List<DutyAddress> getDutyAddress(@Param("start") int start,@Param("end") int end,@Param("addvcd") String addvcd);
}
