package com.sd.modules.system.service;

import com.sd.pojo.DutyRecordPlan;

import java.util.Date;
import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-22 18:26
 */
public interface DutyRecordPlanService {
    /**
     * 获取当天的排班情况
     * @param date
     * @return
     */
    DutyRecordPlan getDutyRecordPlanToday(Date date,String addvcd);

    /**
     * 保存排班表
     * @param dutyRecordPlan
     * @return
     */
    int saveDutyPlan(DutyRecordPlan dutyRecordPlan);
}
