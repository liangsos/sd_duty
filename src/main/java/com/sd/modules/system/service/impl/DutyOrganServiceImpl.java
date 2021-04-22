package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyOrganMapper;
import com.sd.modules.system.service.DutyOrganService;
import com.sd.pojo.DutyOrgan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 8:50
 */
@Service
public class DutyOrganServiceImpl implements DutyOrganService {

    @Autowired
    private DutyOrganMapper dutyOrganMapper;

    /**
     * 获取机构列表
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<DutyOrgan> getDutyOrgan(int start, int end) {
        List<DutyOrgan> list = dutyOrganMapper.getDutyOrgan(start,end);
        return list;
    }

    /**
     * 机构总数
     * @return
     */
    @Override
    public int getDutyOrganCount() {
        QueryWrapper<DutyOrgan> qw = new QueryWrapper<>();
        int count = dutyOrganMapper.selectCount(qw);
        return count;
    }

    /**
     * 校验机构名称
     * @param name
     * @return
     */
    @Override
    public boolean checkOrgan(String name) {
        boolean res = false;
        QueryWrapper<DutyOrgan> qw = new QueryWrapper<>();
        qw.select().eq("name",name);
        DutyOrgan dutyOrgan = dutyOrganMapper.selectOne(qw);
        if (dutyOrgan != null){
            res = true;
        }
        return res;
    }

    /**
     * 新增或修改机构
     * @param saveOrgan
     * @return
     */
    @Override
    public int saveDutyOrgan(DutyOrgan saveOrgan) {
        int res = 0;
        Integer id = saveOrgan.getId();
        if (id == null){//新增
            int insert = dutyOrganMapper.insert(saveOrgan);
            if (insert > 0){
                res = saveOrgan.getId();
            }
        }else {//修改
            int update = dutyOrganMapper.updateById(saveOrgan);
            if (update > 0){
                res = saveOrgan.getId();
            }
        }
        return res;
    }

    /**
     * 删除机构
     * @param id
     * @return
     */
    @Override
    public boolean delDutyOrgan(String id) {
        boolean flag = false;
        int res = dutyOrganMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 获取值班机构信息
     * @param id
     * @return
     */
    @Override
    public DutyOrgan getDutyOrganById(String id) {
        DutyOrgan dutyOrgan = dutyOrganMapper.selectById(id);
        return dutyOrgan;
    }
}
