package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyRecordDetailMapper;
import com.sd.modules.system.service.DutyRecordDetailService;
import com.sd.pojo.DutyRecordDetail;
import com.sd.realm.AuthRealm;
import com.sd.util.ToolUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Chen Hualiang
 * @create 2020-10-20 11:08
 */
@Service
public class DutyRecordDetailServiceImpl implements DutyRecordDetailService {
    @Autowired
    private DutyRecordDetailMapper dutyRecordDetailMapper;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DecimalFormat def = new DecimalFormat("#.#");//保留一位小数

//    private AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
//    private String addvcd = user.getAddvcd();

    @Override
    public boolean addDutyRecordDetail(Date time, String member, String type) {
        boolean flag = true;
        String today = ToolUtil.getToday();
        String beginTime = df.format(time) + " 08:00:00";
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Date nowtime = cal.getTime();//系统当前时间
        List<String> members = new ArrayList<>();
        if (StringUtils.isNotBlank(member)){
            members = Arrays.asList(member.split(","));
        }
        for (String member1 : members) {
            if (StringUtils.isBlank(member1)){
                continue;
            }
            //新增值班记录时 直接插入数据
            if(type.equals("1")){
                DutyRecordDetail dutyRecordDetail = new DutyRecordDetail();
                dutyRecordDetail.setTime(time);
                dutyRecordDetail.setMember(member1);
                dutyRecordDetail.setAddvcd(addvcd);
                try {
                    dutyRecordDetail.setBeginTime(df1.parse(beginTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int res = dutyRecordDetailMapper.insert(dutyRecordDetail);
                if (res <= 0){
                    flag = false;
                }
            }else {//修改值班记录 添加值班人员
                //先判断当天是否存在详细
                QueryWrapper<DutyRecordDetail> qw = new QueryWrapper<>();
                qw.select().eq("time",time).eq("member",member1).eq("addvcd",addvcd);
                List<DutyRecordDetail> details = dutyRecordDetailMapper.selectList(qw);
                if (details.size() > 0){//存在记录时 更新原纪录
                    //只有修改当天的记录才更新 历史记录不更新
                    if (today.equals(df.format(time))){
                        QueryWrapper<DutyRecordDetail> updateQw = new QueryWrapper<>();
                        updateQw.eq("time",time);
                        DutyRecordDetail detail = new DutyRecordDetail();
                        detail.setEndTime(nowtime);
                        int res = dutyRecordDetailMapper.update(detail,updateQw);
                        if (res <= 0){
                            flag = false;
                        }
                    }
                }else {//不存在 插入数据
                    String timeData = df.format(time);
                    //日期小于今天时 插入一条完整的记录
                    int comp = timeData.compareTo(today);
                    int res;
                    if (comp < 0){
                        DutyRecordDetail detail = new DutyRecordDetail();
                        detail.setTime(time);
                        detail.setMember(member1);
                        detail.setAddvcd(addvcd);
                        try {
                            detail.setBeginTime(df1.parse(beginTime));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(time);
                        calendar.add(Calendar.DATE,1);
                        detail.setEndTime(calendar.getTime());
                        detail.setHours(BigDecimal.valueOf(24));
                        detail.setDays(BigDecimal.valueOf(1));
                        detail.setFlag("1");
                        res = dutyRecordDetailMapper.insert(detail);
                    }else if (comp == 0){//为今天时
                        DutyRecordDetail detail = new DutyRecordDetail();
                        detail.setTime(time);
                        detail.setMember(member1);
                        detail.setBeginTime(nowtime);
                        detail.setAddvcd(addvcd);
                        res = dutyRecordDetailMapper.insert(detail);
                    }else {//日期大于今天时
                        DutyRecordDetail detail = new DutyRecordDetail();
                        detail.setTime(time);
                        detail.setMember(member1);
                        detail.setAddvcd(addvcd);
                        try {
                            detail.setBeginTime(df1.parse(beginTime));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        res = dutyRecordDetailMapper.insert(detail);
                    }

                    if (res <= 0){
                        flag =false;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 更新值班记录详细表
     * @param time 日期
     * @param member 值班人员
     * @param type  类型 1调班 （直接删除）
     * @return
     */
    @Override
    public boolean updateDutyRecordDetail(Date time, String member, String type) {
        boolean flag = true;
        int res;
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Date nowtime = cal.getTime();//系统当前时间

        String today = ToolUtil.getToday();
        List<String> members = new ArrayList<>();
        if (StringUtils.isNotBlank(member)){
            members = Arrays.asList(member.split(","));
        }
        for (String singleMember : members) {
            if (StringUtils.isBlank(singleMember)){
                continue;
            }
            //修改日期不等于今天时 直接删除
            //修改日期小于今天时 任务历史的值班记录无效
            //修改日期大于今天时 值班正常跳转人员 直接删除
            if (type == "1"){
                QueryWrapper<DutyRecordDetail> qw = new QueryWrapper<>();
                qw.eq("time",time).eq("member",singleMember).eq("addvcd",addvcd);
                res = dutyRecordDetailMapper.delete(qw);
            }else {
                String paramTime = df.format(time);
                int comp = paramTime.compareTo(today);
                if (comp == 0){//日期为今天时 更新记录
                    //先获取记录
                    QueryWrapper<DutyRecordDetail> qw = new QueryWrapper<>();
                    qw.select().eq("time",time).eq("member",singleMember).eq("addvcd",addvcd);
                    DutyRecordDetail detail = dutyRecordDetailMapper.selectOne(qw);
                    if (detail == null){
                        continue;
                    }
                    int id = detail.getId();
                    Date tempTime;
                    double days = 0;
                    Date beginTime = detail.getBeginTime();
                    Date endTime = detail.getEndTime();
                    String hours = String.valueOf(detail.getHours());
                    //结束时间不为空以其计算 否则以开始时间计算小时数
                    if(endTime == null){
                        tempTime = beginTime;
                    }else {
                        tempTime = endTime;
                    }
                    //根据时间获取小时数
                    double tempHour = ToolUtil.getDutyHoursNow(tempTime);
                    tempHour = Double.parseDouble(def.format(tempHour));
                    if (StringUtils.isBlank(hours) && Double.parseDouble(hours) > 0){
                        tempHour += Double.parseDouble(hours);
                    }
                    if (tempHour > 24){//超过24小时 变为24小时
                        tempHour = 24;
                    }
                    if (tempHour > 0){

                        days =  Double.parseDouble(def.format(tempHour / 24)) ;
                    }
                    QueryWrapper<DutyRecordDetail> updateQw = new QueryWrapper<>();
                    DutyRecordDetail detailUpdate = new DutyRecordDetail();
                    if (endTime == null){
                        detailUpdate.setEndTime(nowtime);
                    }
                    detailUpdate.setHours(BigDecimal.valueOf(tempHour));
                    detailUpdate.setDays(BigDecimal.valueOf(days));
                    detailUpdate.setFlag("1");
                    updateQw.eq("id",id);
                    res = dutyRecordDetailMapper.update(detailUpdate,updateQw);
//                    res = dutyRecordDetailMapper.updateRecordDetail(nowtime,tempHour,days,id);
                }else {//直接删除
                    QueryWrapper<DutyRecordDetail> qw = new QueryWrapper<>();
                    qw.eq("time",time).eq("member",singleMember).eq("addvcd",addvcd);
                    res = dutyRecordDetailMapper.delete(qw);
                }
            }
            if (res < 0){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean updateDutyRecordDetail() {
        boolean res = true;
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();

        //获取未更新的列
        String time = "";
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        if(cal.get(Calendar.HOUR_OF_DAY) > 8){
            time = df.format(cal.getTime());
        }else {
            cal.add(Calendar.DAY_OF_MONTH,-1);
            time = df.format(cal.getTime());
        }
        //统计次数时 只统计交过班的人员记录
        List<Map<String,Object>> list = dutyRecordDetailMapper.selectDutyTj(time,addvcd);
        if (list.size() > 0){
            for (Map<String, Object> map : list) {
                String id = String.valueOf(map.get("id"));
                double days = 0;
                Date tempTime;
                Date beginTime = null;
                Date endTime = null;
                try {
                    if (map.get("beginTime") != null){
                        beginTime = df1.parse(df1.format(map.get("beginTime")));
                    }
                    if (map.get("endTime") !=null){
                        endTime = df1.parse(df1.format(map.get("endTime")));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                Date endTime = map.get("endTime");
                String hours = String.valueOf(map.get("hours"));
                if (endTime==null){
                    tempTime = beginTime;
                }else {
                    tempTime = endTime;
                }
                double tempHour = ToolUtil.getDutyHoursNow(tempTime);
                if (StringUtils.isBlank(hours) && Double.parseDouble(hours) > 0){
                    tempHour += Double.parseDouble(hours);
                }
                if (tempHour > 24){//超过24小时变成24小时
                    tempHour = 24;
                }
                if (tempHour > 0){

                    days =  Double.parseDouble(def.format(tempHour / 24)) ;
                }
                //更新原先的记录
                QueryWrapper<DutyRecordDetail> updateQw = new QueryWrapper<>();
                DutyRecordDetail detailUpdate = new DutyRecordDetail();
                if (endTime == null){
                    detailUpdate.setEndTime(ToolUtil.getDutyEndTime(beginTime));
                }
                detailUpdate.setHours(BigDecimal.valueOf(tempHour));
                detailUpdate.setDays(BigDecimal.valueOf(days));
                detailUpdate.setFlag("1");
                detailUpdate.setAddvcd(addvcd);
                updateQw.eq("id",id);
                int tempRes = dutyRecordDetailMapper.update(detailUpdate,updateQw);
                if (tempRes < 0) {
                    res = false;
                }
            }
        }
        return res;
    }


}
