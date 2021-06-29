package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.CommunicationMapper;
import com.sd.modules.system.mapper.DutyRecordMapper;
import com.sd.modules.system.service.PhoneService;
import com.sd.pojo.Communication;
import com.sd.pojo.DutyRecord;
import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2021-06-29 12:39
 */
@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private CommunicationMapper communicationMapper;
    @Autowired
    private DutyRecordMapper dutyRecordMapper;

    @Override
    public List<Communication> getCommunication() {
        String addvcd = "37";
        QueryWrapper<Communication> qw = new QueryWrapper<>();
        qw.select().eq("addvcd",addvcd);
        List<Communication> list = communicationMapper.selectList(qw);
        return list;
    }

    @Override
    public int getCommunicationCount() {
        String addvcd = "37";
        QueryWrapper<Communication> qw = new QueryWrapper<>();
        qw.eq("addvcd",addvcd);
        int count = communicationMapper.selectCount(qw);
        return count;
    }

    @Override
    public List<DutyRecord> getDutyBb(String beginTime, String endTime, int start, int end) {
        String addvcd = "37";
        List<DutyRecord> list = dutyRecordMapper.getDutyBb(beginTime,endTime,start,end,addvcd);
        return list;
    }

    @Override
    public int getDutyBbCount(String beginTime, String endTime) {
        String addvcd = "37";
        QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
        qw.eq("addvcd",addvcd).between("time",beginTime,endTime);
        int count = dutyRecordMapper.selectCount(qw);
        return count;
    }
}
