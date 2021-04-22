package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyRecordDetailMapper;
import com.sd.modules.system.mapper.DutyRecordMapper;
import com.sd.modules.system.mapper.DutyRecordPlanMapper;
import com.sd.modules.system.service.DutyRecordUserService;
import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordDetail;
import com.sd.pojo.DutyRecordPlan;
import com.sd.pojo.DutyTj;
import com.sd.realm.AuthRealm;
import com.sd.util.ToolUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.MaskFormatter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Chen Hualiang
 * @create 2020-10-19 9:40
 */
@Service
public class DutyRecordUserServiceImpl implements DutyRecordUserService {

    @Autowired
    private DutyRecordPlanMapper dutyRecordPlanMapper;
    @Autowired
    private DutyRecordMapper dutyRecordMapper;
    @Autowired
    private DutyRecordDetailMapper dutyRecordDetailMapper;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<DutyRecordPlan> getDutyRecordPlan(String beginTime, String endTime,String addvcd) {
        QueryWrapper<DutyRecordPlan> qw = new QueryWrapper<>();
        qw.eq("addvcd",addvcd).between("time",beginTime,endTime);
        List<DutyRecordPlan> list = dutyRecordPlanMapper.selectList(qw);
        return list;
    }

    @Override
    public List<DutyRecord> getDutyRecord(String beginTime, String endTime,String addvcd) {
        QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
        qw.eq("addvcd",addvcd).between("time",beginTime,endTime);
        List<DutyRecord> list = dutyRecordMapper.selectList(qw);
        return list;
    }

    @Override
    public List<DutyRecord> getDutyRecordToday(String time,String addvcd) {
        QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
        qw.eq("time",time).eq("addvcd",addvcd);
        List<DutyRecord> list = dutyRecordMapper.selectList(qw);
        return list;
    }

    @Override
    public List<DutyRecord> getDutyRecordById(String id) {
        QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
        qw.eq("id",id);
        List<DutyRecord> list = dutyRecordMapper.selectList(qw);
        return list;
    }

    @Override
    public int saveDutyRecord(DutyRecord dutyRecord) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getuserName();
        String addvcd = user.getAddvcd();
        dutyRecord.setUpdateUser(userName);
        dutyRecord.setAddvcd(addvcd);
        String id = String.valueOf(dutyRecord.getId());
        int i;
        if (id.equals("null")){
            i = dutyRecordMapper.insert(dutyRecord);
        }else {
            i = dutyRecordMapper.updateById(dutyRecord);
        }

        return dutyRecord.getId();
    }

    @Override
    public List<Map<String, Object>> getDutyTj(String beginTime, String endTime) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<Map<String,Object>> tjList = new ArrayList<>();
        //获取所有用户姓名id及角色
        List<Map<String,Object>> list = dutyRecordMapper.getDutyTj(addvcd);

        //先获取详细表记录 新建详细表 列为时间 每一天
        List<Map<String,Object>> dataList = new ArrayList<>();

        //开始时间的年月日
        Calendar beginCal=Calendar.getInstance();
        try {
            beginCal.setTime(df.parse(beginTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int beginYear = beginCal.get(Calendar.YEAR);
        int beginMonth = beginCal.get(Calendar.MONTH) + 1;
        int beginDay = beginCal.get(Calendar.DAY_OF_MONTH);
        //结束时间的年月日
        Calendar endCal = Calendar.getInstance();
        try {
            endCal.setTime(df.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int endYear = endCal.get(Calendar.YEAR);
        int endMonth = endCal.get(Calendar.MONTH) + 1;
        int endDay = endCal.get(Calendar.DAY_OF_MONTH);

        //开始到结束之间的月份
        List<Integer> monthList = new ArrayList<>();
        for (int i= beginMonth; i <= endMonth; i++){
            monthList.add(i);
        }

        for (Map<String, Object> map : list){
            Map<String,Object> maps = new HashMap<>();
            String name = map.get("name").toString();
            String type = map.get("type").toString();
            maps.put("姓名",name);
            maps.put("角色",type);
            int allWorkdaySum = 0;
            int allHoildaySum = 0;
            for (Integer integer : monthList){
                //获取期间每个月份的工作日和周末集合
                List<Date> workDays = ToolUtil.getWorkDays(beginYear,integer);
                List<String> workDayString = ToolUtil.getDateString(workDays);
                List<Date> hoildays = ToolUtil.getHoildays(beginYear,integer);
                List<String> hoildayString = ToolUtil.getDateString(hoildays);
                //选择时间段内的工作日集合
                workDayString.removeIf(workday -> beginTime.compareTo(workday) > 0);
                workDayString.removeIf(workday -> endTime.compareTo(workday) < 0);
                //选择时间段内的节假日集合
                hoildayString.removeIf(hoilday -> beginTime.compareTo(hoilday) > 0);
                hoildayString.removeIf(hoilday -> endTime.compareTo(hoilday) < 0);
                int workdaySum = 0;
                for (String workday : workDayString) {//工作日值班情况
                    int temp = 0;
                    if (type.equals("3")){//普通值班人员
                        temp = getDutyCountByUser(name,workday);
                    }else {//带班或委领导
                        temp = getDutyCountByLeader(name,workday,type);
                    }
                    //工作日值班统计
                    if (temp == 1){
                        workdaySum += 1;
                    }
                }
                maps.put(integer + "月工作日",workdaySum);

                int hoildaySum = 0;
                for (String hoilday : hoildayString) {
                    int temp = 0;
                    if (type.equals("3")){//普通值班人员
                        temp = getDutyCountByUser(name,hoilday);
                    }else {//带班或委领导
                        temp = getDutyCountByLeader(name,hoilday,type);
                    }
                    //节假日
                    if (temp == 1){
                        hoildaySum += 1;
                    }
                }
                maps.put(integer + "月节假日",hoildaySum);
                if (workdaySum > 0){
                    allWorkdaySum += workdaySum;
                }
                maps.put("工作日合计",allWorkdaySum);
                if (hoildaySum > 0){
                    allHoildaySum += hoildaySum;
                }
                maps.put("节假日合计",allHoildaySum);
            }
            tjList.add(maps);
        }

        return tjList;
    }

    /**
     * 获取带班或者委领导某一天是否值班
     * @param name
     * @param date
     * @param type
     * @return
     */
    private int getDutyCountByLeader(String name, String date, String type) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();

        //带班或者委领导 认为只要排班了就统计次数  他们不来值班室只是有事电话联系他们
        int total = 0;
        int count = 0;
        if (type.equals("1")){
            QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
            qw.select().eq("time",date).like("leaderComm",name).eq("addvcd",addvcd);
            count = dutyRecordMapper.selectCount(qw);
        }else {
            QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
            qw.select().eq("time",date).like("leader",name).eq("addvcd",addvcd);
            count = dutyRecordMapper.selectCount(qw);
        }
        if (count != 0){
            total = 1;
        }
        return total;
    }

    /**
     * 获取人员某一天是否值班
     * @param name
     * @param date
     * @return
     */
    private int getDutyCountByUser(String name, String date) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        int total = 0;
        QueryWrapper<DutyRecordDetail> qw = new QueryWrapper<>();
        qw.select("days").eq("time",date).eq("member",name).eq("flag","1").eq("addvcd",addvcd);
        DutyRecordDetail detail = dutyRecordDetailMapper.selectOne(qw);
        if (detail != null){
            double days = Double.parseDouble(String.valueOf(detail.getDays()));
            if (days > 0){
                total = 1;
            }
        }
        return total;
    }
}
