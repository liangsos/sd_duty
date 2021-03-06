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
        //????????????????????????id?????????
        List<Map<String,Object>> list = dutyRecordMapper.getDutyTj(addvcd);

        //???????????????????????? ??????????????? ???????????? ?????????
        List<Map<String,Object>> dataList = new ArrayList<>();

        //????????????????????????
        Calendar beginCal=Calendar.getInstance();
        try {
            beginCal.setTime(df.parse(beginTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int beginYear = beginCal.get(Calendar.YEAR);
        int beginMonth = beginCal.get(Calendar.MONTH) + 1;
        int beginDay = beginCal.get(Calendar.DAY_OF_MONTH);
        //????????????????????????
        Calendar endCal = Calendar.getInstance();
        try {
            endCal.setTime(df.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int endYear = endCal.get(Calendar.YEAR);
        int endMonth = endCal.get(Calendar.MONTH) + 1;
        int endDay = endCal.get(Calendar.DAY_OF_MONTH);

        //??????????????????????????????
        List<Integer> monthList = new ArrayList<>();
        for (int i= beginMonth; i <= endMonth; i++){
            monthList.add(i);
        }

        for (Map<String, Object> map : list){
            Map<String,Object> maps = new HashMap<>();
            String name = map.get("name").toString();
            String type = map.get("type").toString();
            maps.put("??????",name);
            maps.put("??????",type);
            int allWorkdaySum = 0;
            int allHoildaySum = 0;
            for (Integer integer : monthList){
                //???????????????????????????????????????????????????
                List<Date> workDays = ToolUtil.getWorkDays(beginYear,integer);
                List<String> workDayString = ToolUtil.getDateString(workDays);
                List<Date> hoildays = ToolUtil.getHoildays(beginYear,integer);
                List<String> hoildayString = ToolUtil.getDateString(hoildays);
                //????????????????????????????????????
                workDayString.removeIf(workday -> beginTime.compareTo(workday) > 0);
                workDayString.removeIf(workday -> endTime.compareTo(workday) < 0);
                //????????????????????????????????????
                hoildayString.removeIf(hoilday -> beginTime.compareTo(hoilday) > 0);
                hoildayString.removeIf(hoilday -> endTime.compareTo(hoilday) < 0);
                int workdaySum = 0;
                for (String workday : workDayString) {//?????????????????????
                    int temp = 0;
                    if (type.equals("3")){//??????????????????
                        temp = getDutyCountByUser(name,workday);
                    }else {//??????????????????
                        temp = getDutyCountByLeader(name,workday,type);
                    }
                    //?????????????????????
                    if (temp == 1){
                        workdaySum += 1;
                    }
                }
                maps.put(integer + "????????????",workdaySum);

                int hoildaySum = 0;
                for (String hoilday : hoildayString) {
                    int temp = 0;
                    if (type.equals("3")){//??????????????????
                        temp = getDutyCountByUser(name,hoilday);
                    }else {//??????????????????
                        temp = getDutyCountByLeader(name,hoilday,type);
                    }
                    //?????????
                    if (temp == 1){
                        hoildaySum += 1;
                    }
                }
                maps.put(integer + "????????????",hoildaySum);
                if (workdaySum > 0){
                    allWorkdaySum += workdaySum;
                }
                maps.put("???????????????",allWorkdaySum);
                if (hoildaySum > 0){
                    allHoildaySum += hoildaySum;
                }
                maps.put("???????????????",allHoildaySum);
            }
            tjList.add(maps);
        }

        return tjList;
    }

    /**
     * ????????????????????????????????????????????????
     * @param name
     * @param date
     * @param type
     * @return
     */
    private int getDutyCountByLeader(String name, String date, String type) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();

        //????????????????????? ????????????????????????????????????  ???????????????????????????????????????????????????
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
     * ?????????????????????????????????
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
