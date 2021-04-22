package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyRoleMapper;
import com.sd.modules.system.service.DutyRoleService;
import com.sd.pojo.DutyRole;
import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chen Hualiang
 * @create 2020-12-18 17:41
 */
@Service
public class DutyRoleServiceImpl implements DutyRoleService {
    @Autowired
    private DutyRoleMapper dutyRoleMapper;

    @Override
    public DutyRole findRoleByUserId(Integer userId) {
//        QueryWrapper<DutyRole> qw = new QueryWrapper<>();
//        qw.select().eq("user_id",userId);
        DutyRole dutyRole = dutyRoleMapper.selectById(userId);
        return dutyRole;
    }

    @Override
    public int getDutyUserSort(String userType) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        if (userType.contains("1")){
            userType = "1";
        }
        if (userType.contains("2")){
            userType = "2";
        }else {
            userType = "3";
        }
        int sort = dutyRoleMapper.getDutyUserSort(userType,addvcd);
        return sort;
    }

    @Override
    public long saveDutyRole(DutyRole dutyRole) {
        long res = 0;
        res = dutyRoleMapper.insert(dutyRole);
        return res;
    }

    @Override
    public long updateDutyRole(DutyRole dutyRole) {
        long res = 0;
        res = dutyRoleMapper.updateById(dutyRole);
        return res;
    }
}
