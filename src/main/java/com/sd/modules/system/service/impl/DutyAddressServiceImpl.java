package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyAddressMapper;
import com.sd.modules.system.service.DutyAddressService;
import com.sd.pojo.DutyAddress;
import com.sd.pojo.DutyHoliday;
import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-28 10:53
 */
@Service
public class DutyAddressServiceImpl implements DutyAddressService {
    @Autowired
    private DutyAddressMapper dutyAddressMapper;

    @Override
    public List<DutyAddress> getDutyAddress(int start, int end) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyAddress> list = dutyAddressMapper.getDutyAddress(start,end,addvcd);
        return list;
    }

    @Override
    public int getDutyAddressCount() {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        QueryWrapper<DutyAddress> qw = new QueryWrapper<>();
        qw.eq("addvcd",addvcd);
        int count = dutyAddressMapper.selectCount(qw);
        return count;
    }

    @Override
    public boolean checkAddress(String address) {
        boolean res = false;
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        QueryWrapper<DutyAddress> qw = new QueryWrapper<>();
        qw.select().eq("address",address).eq("addvcd",addvcd);
        DutyAddress address1 = dutyAddressMapper.selectOne(qw);
        if (address1 != null){
            res = true;
        }
        return res;
    }

    @Override
    public int saveAddress(DutyAddress saveAddress) {
        int res = 0;
        Integer id = saveAddress.getId();
        if (id == null){//新增
            int insert = dutyAddressMapper.insert(saveAddress);
            if (insert > 0){
                res = saveAddress.getId();
            }
        }else {//修改
            int update = dutyAddressMapper.updateById(saveAddress);
            if (update > 0){
                res = saveAddress.getId();
            }
        }
        return res;
    }

    @Override
    public boolean delAddress(String id) {
        boolean flag = false;
        int res = dutyAddressMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public DutyAddress getAddressInfo(String id) {
        DutyAddress address = dutyAddressMapper.selectById(id);
        return address;
    }
}
