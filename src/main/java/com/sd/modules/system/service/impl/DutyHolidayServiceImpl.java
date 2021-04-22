package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyHolidayMapper;
import com.sd.modules.system.service.DutyHolidayService;
import com.sd.pojo.DutyFileOrgan;
import com.sd.pojo.DutyHoliday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 17:51
 */
@Service
public class DutyHolidayServiceImpl implements DutyHolidayService {

    @Autowired
    private DutyHolidayMapper dutyHolidayMapper;
    /**
     * 获取法定节假日列表
     * @param searchYear
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<DutyHoliday> getDutyHoliday(String searchYear, int start, int end) {
        List<DutyHoliday> list = dutyHolidayMapper.getDutyHoliday(searchYear,start,end);
        return list;
    }

    /**
     * 法定节假日总数
     * @param searchYear
     * @return
     */
    @Override
    public int getDutyHolidayCount(String searchYear) {
        int count = dutyHolidayMapper.getDutyHolidayCount(searchYear);
        return count;
    }

    /**
     * 校验日期是否重复
     * @param date
     * @return
     */
    @Override
    public boolean checkHoliday(String date) {
        boolean res = false;
        QueryWrapper<DutyHoliday> qw = new QueryWrapper<>();
        qw.select().eq("time",date);
        DutyHoliday holiday = dutyHolidayMapper.selectOne(qw);
        if (holiday != null){
            res = true;
        }
        return res;
    }

    @Override
    public int saveHoliday(DutyHoliday saveHoliday) {
        int res = 0;
        Integer id = saveHoliday.getId();
        if (id == null){//新增
            int insert = dutyHolidayMapper.insert(saveHoliday);
            if (insert > 0){
                res = saveHoliday.getId();
            }
        }else {//修改
            int update = dutyHolidayMapper.updateById(saveHoliday);
            if (update > 0){
                res = saveHoliday.getId();
            }
        }
        return res;
    }

    @Override
    public boolean delHoliday(String id) {
        boolean flag = false;
        int res = dutyHolidayMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public DutyHoliday getHolidayInfo(String id) {
        DutyHoliday dutyHoliday = dutyHolidayMapper.selectById(id);
        return dutyHoliday;
    }
}
