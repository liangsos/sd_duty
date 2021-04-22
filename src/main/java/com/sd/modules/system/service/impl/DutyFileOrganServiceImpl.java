package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyFileOrganMapper;
import com.sd.modules.system.service.DutyFileOrganService;
import com.sd.pojo.DutyFileOrgan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 10:06
 */
@Service
public class DutyFileOrganServiceImpl implements DutyFileOrganService {
    @Autowired
    private DutyFileOrganMapper dutyFileOrganMapper;

    /**
     * 获取来文单位列表
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<DutyFileOrgan> getDutyFileOrgan(int start, int end) {
        List<DutyFileOrgan> list = dutyFileOrganMapper.getDutyFileOrgan(start,end);
        return list;
    }

    @Override
    public int getDutyFileOrganCount() {
        QueryWrapper<DutyFileOrgan> qw = new QueryWrapper<>();
        int count = dutyFileOrganMapper.selectCount(qw);
        return count;
    }

    @Override
    public int getDutyFileSort() {
        int res = 1;
        int sort = dutyFileOrganMapper.getSort();
        if (sort !=0){
            res = sort + 1;
        }
        return res;
    }

    /**
     * 校验来文单位名称
     * @param name
     * @return
     */
    @Override
    public boolean checkFileOrgan(String name) {
        boolean res = false;
        QueryWrapper<DutyFileOrgan> qw = new QueryWrapper<>();
        qw.select().eq("name",name);
        DutyFileOrgan fileOrgan = dutyFileOrganMapper.selectOne(qw);
        if (fileOrgan != null){
            res = true;
        }
        return res;
    }

    @Override
    public int saveDutyOrgan(DutyFileOrgan saveOrgan) {
        int res = 0;
        Integer id = saveOrgan.getId();
        if (id == null){//新增
            int insert = dutyFileOrganMapper.insert(saveOrgan);
            if (insert > 0){
                res = saveOrgan.getId();
            }
        }else {//修改
            int update = dutyFileOrganMapper.updateById(saveOrgan);
            if (update > 0){
                res = saveOrgan.getId();
            }
        }
        return res;
    }

    @Override
    public boolean delDutyFileOrgan(String id) {
        boolean flag = false;
        int res = dutyFileOrganMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public DutyFileOrgan getDutyFileOrganById(String id) {
        DutyFileOrgan dutyFileOrgan = dutyFileOrganMapper.selectById(id);
        return dutyFileOrgan;
    }
}
