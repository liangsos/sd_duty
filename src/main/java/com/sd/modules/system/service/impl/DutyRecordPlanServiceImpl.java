package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyRecordPlanMapper;
import com.sd.modules.system.service.DutyRecordPlanService;
import com.sd.pojo.DutyRecordPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-22 18:28
 */
@Service
public class DutyRecordPlanServiceImpl implements DutyRecordPlanService {
    @Autowired
    private DutyRecordPlanMapper dutyRecordPlanMapper;

    /**
     * 获取当天排班情况
     * @param date
     * @return
     */
    @Override
    public DutyRecordPlan getDutyRecordPlanToday(Date date,String addvcd) {
        QueryWrapper<DutyRecordPlan> qw = new QueryWrapper<>();
        qw.select().eq("time",date).eq("addvcd",addvcd);
        DutyRecordPlan plan = dutyRecordPlanMapper.selectOne(qw);
        return plan;
    }

    @Override
    public int saveDutyPlan(DutyRecordPlan dutyRecordPlan) {
//        int id = String.valueOf(dutyRecordPlan.getId());
        int res = 0;
        if (dutyRecordPlan.getId() == 0){//无历史排班记录 插入数据
//            QueryWrapper<DutyRecordPlan> qw = new QueryWrapper<>();
            res = dutyRecordPlanMapper.insert(dutyRecordPlan);
        }else {//存在历史记录 更新数据
            res = dutyRecordPlanMapper.updateById(dutyRecordPlan);
        }
        return dutyRecordPlan.getId();
    }
}
