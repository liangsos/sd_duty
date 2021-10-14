package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.AddvcdStcdMapper;
import com.sd.modules.system.service.CommunicationNewService;
import com.sd.pojo.AddvcdStcd;
import com.sd.pojo.Communication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationNewServiceImpl implements CommunicationNewService {

    @Autowired
    private AddvcdStcdMapper addvcdStcdMapper;

    @Override
    public List<AddvcdStcd> getAddvcdStcd() {
        List<AddvcdStcd> list = addvcdStcdMapper.selectList(null);
        return list;
    }
}
