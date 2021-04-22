package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.CommunicationMapper;
import com.sd.modules.system.service.CommunicationService;
import com.sd.pojo.Communication;
import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-11-12 11:22
 */
@Service
public class CommunicationServiceImpl implements CommunicationService {
    @Autowired
    private CommunicationMapper communicationMapper;

    @Override
    public List<Communication> getCommunication() {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        QueryWrapper<Communication> qw = new QueryWrapper<>();
        qw.select().eq("addvcd",addvcd);
        List<Communication> list = communicationMapper.selectList(qw);
        return list;
    }

    @Override
    public int getCommunicationCount() {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        QueryWrapper<Communication> qw = new QueryWrapper<>();
        qw.eq("addvcd",addvcd);
        int count = communicationMapper.selectCount(qw);
        return count;
    }

    @Override
    public int saveCommunication(Communication saveComm) {
        int res = 0;
        Integer id = saveComm.getId();
        if (id == null){//新增
            int insert = communicationMapper.insert(saveComm);
            if (insert > 0){
                res = saveComm.getId();
            }
        }else {//修改
            int update = communicationMapper.updateById(saveComm);
            if (update > 0){
                res = saveComm.getId();
            }
        }
        return res;
    }

    @Override
    public boolean delCommunication(String id) {
        boolean flag = false;
        int res = communicationMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public Communication getCommunicationById(String id) {
        Communication communication =communicationMapper.selectById(id);
        return communication;
    }
}
