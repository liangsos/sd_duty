package com.sd.modules.system.service;

import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordPlan;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2020-10-19 9:39
 */
public interface DutyRecordUserService {

    /**
     * 根据时间获取排班表
     * @param beginTime
     * @param endTime
     * @return
     */
    List<DutyRecordPlan> getDutyRecordPlan(String beginTime, String endTime,String addvcd);

    /**
     * 根据时间获取值班记录
     * @param beginTime
     * @param endTime
     * @return
     */
    List<DutyRecord> getDutyRecord(String beginTime, String endTime,String addvcd);

    List<DutyRecord> getDutyRecordToday(String time,String addvcd);

    /**
     * 根据ID获取值班记录
     * @param id
     * @return
     */
    List<DutyRecord> getDutyRecordById(String id);

    /**
     * 保存值班记录 更新/新增
     * @param dutyRecord
     * @return
     */
    int saveDutyRecord(DutyRecord dutyRecord);

    /**
     * 值班统计
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> getDutyTj(String beginTime, String endTime);
}
