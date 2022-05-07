package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.AddvcdStcdMapper;
import com.sd.modules.system.mapper.CommunicationMapper;
import com.sd.modules.system.service.CommunicationNewService;
import com.sd.pojo.AddvcdStcd;
import com.sd.pojo.Communication;
import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationNewServiceImpl implements CommunicationNewService {

    @Autowired
    private AddvcdStcdMapper addvcdStcdMapper;
    @Autowired
    private CommunicationMapper communicationMapper;

    @Override
    public List<AddvcdStcd> getAddvcdStcd() {
        List<AddvcdStcd> list = addvcdStcdMapper.selectList(null);
        return list;
    }

    @Override
    public List<Communication> getCommunicationNew(String addvcd) {
        if (addvcd.equals("")){
            addvcd = "37";
        }
        QueryWrapper<Communication> qw = new QueryWrapper<>();
        qw.select().eq("addvcd",addvcd).orderByAsc("sort");
        List<Communication> list = communicationMapper.selectList(qw);
        return list;
    }
}
