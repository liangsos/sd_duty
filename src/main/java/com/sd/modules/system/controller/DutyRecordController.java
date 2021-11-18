package com.sd.modules.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.sd.modules.system.entity.Log;
import com.sd.modules.system.service.*;
import com.sd.modules.system.vo.DutyRecordTjVO;
import com.sd.modules.system.vo.SaveDutyRecordTelVO;
import com.sd.pojo.*;
import com.sd.realm.AuthRealm;
import com.sd.util.RestResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dutyRecord")
@CrossOrigin
@Api(tags = "值班记录")
public class DutyRecordController {

    @Autowired
    private DutyRecordService dutyRecordService;
    @Autowired
    private DutyBbService dutyBbService;
    @Autowired
    private DutyRecordUserService dutyRecordUserService;
    @Autowired
    private LogService logService;
    @Autowired
    private DutyRecordDetailService dutyRecordDetailService;


    @GetMapping("/getDutyRecord")
    @ApiOperation("根据时间获取值班记录")
    public RestResponse getDutyRecord(String beginTime, String endTime) {
        try {
            List<DutyRecordTjVO> list = dutyRecordService.getDutyRecord(beginTime, endTime);
            return RestResponse.success().setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/getLastDutyUser")
    @ApiOperation("判断当前用户是否为最新的接班人员")
    public RestResponse getLastDutyUser(String time) {
        try {
            boolean dutyUser = dutyRecordService.isDutyUser(time);
            return RestResponse.success().setData(dutyUser);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/getFxkhByTime")
    @ApiOperation("根据时间获取防汛抗旱工作")
    public RestResponse getFxkhByTime(String time) {
        try {
            List<DutyFxkh> fxkhList = dutyRecordService.getFxkhByTime(time);
            return RestResponse.success().setData(fxkhList);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/getDutyRecordTel")
    @ApiOperation("根据时间获取来电记录")
    public RestResponse getDutyRecordTel(String time) {
        try {
            List<DutyRecordTel> dutyRecordTels = dutyBbService.getDutyRecordTel(time);
            return RestResponse.success().setData(dutyRecordTels);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/getDutyStationByType")
    @ApiOperation("根据类别获取雨水情主要站点")
    public RestResponse getDutyStationByType(String type) {
        try {
            List<DutyStation> list = dutyRecordService.getDutyStationByType(type);
            return RestResponse.success().setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/getDutyFileOrganAll")
    @ApiOperation("值班-获取所有的来文单位")
    public RestResponse getDutyFileOrganAll() {
        try {
            List<DutyFileOrgan> list = dutyRecordService.getDutyFileOrganAll();
            return RestResponse.success().setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/getDutyOrgan")
    @ApiOperation("值班机构")
    public RestResponse getDutyOrgan(){
        try {
            List<DutyOrgan> list = dutyRecordService.getDutyOrgan();
            return RestResponse.success().setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveDutyFxkh")
    @ApiOperation("保存防汛抗旱工作")
    public RestResponse saveDutyFxkh(@RequestBody DutyFxkh dutyFxkh) {
        try {
            dutyFxkh = dutyRecordService.saveDutyFxkh(dutyFxkh);
            return RestResponse.success("保存雨水情成功！").setData(dutyFxkh);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveDutyRecordTel")
    @ApiOperation("保存来电记录")
    public RestResponse saveDutyRecordTel(SaveDutyRecordTelVO sdrt) {
        try {
            DutyRecordTel dutyRecordTel = dutyRecordService.saveDutyRecordTel(sdrt);
            return RestResponse.success("保存来电记录成功！").setData(dutyRecordTel);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delDutyRecordTel")
    @ApiOperation("删除来电记录")
    public RestResponse delDutyRecordTel(Integer id) {
        try {
            dutyRecordService.delDutyRecordTel(id);
            return RestResponse.success("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/handOver")
    @ApiOperation("交接班")
    public RestResponse handOver(@RequestParam("today")String today, @RequestParam("member") String member,@RequestParam("member_old") String memberOld ) {
        try {
            int res = 0;
            AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            List<DutyRecord> recordList = dutyRecordUserService.getDutyRecordToday(today,user.getAddvcd());
            DutyRecord dutyRecord = new DutyRecord();
            if (recordList.size() > 0){
                for (DutyRecord record : recordList) {
                    dutyRecord.setLeader(record.getLeader());
                    dutyRecord.setLeaderComm(record.getLeaderComm());
                    dutyRecord.setTime(record.getTime());
                    dutyRecord.setId(record.getId());
                    dutyRecord.setComments(record.getComments());
                    dutyRecord.setDuty("1");
                    dutyRecord.setMember(member);
                    dutyRecord.setUpdateUser(user.getuserName());
                    dutyRecord.setAddvcd(user.getAddvcd());
                }
                res = dutyRecordService.updateDutyFlag(dutyRecord);
                if (res > 0){//添加日志
                    Log log = new Log();
                    log.setTableName("Duty_Record");
                    log.setTabaleId(res);
                    log.setType("3");
                    log.setContent("交接班");
                    log.setUpdateUser(user.getuserName());
                    log.setAddvcd(user.getAddvcd());
                    logService.insertLog(log);

                    if (!member.equals(memberOld)){//添加修改日志并更新值班表
                        Log log1 = new Log();
                        log1.setTableName("Duty_Record");
                        log1.setTabaleId(res);
                        log1.setType("3");
                        log1.setUpdateUser(user.getuserName());
                        String detail = "值班人员：旧（" + memberOld + "）新" + member + "）";
                        log1.setContent(detail);
                        log1.setAddvcd(user.getAddvcd());
                        logService.insertLog(log);

                        //更新值班详情表
                        List<String> membersOld = new ArrayList<>();
                        List<String> membersNew = new ArrayList<>();
                        membersOld = Arrays.asList(memberOld.split(","));
                        membersNew = Arrays.asList(member.split(","));

                        //找出新增的值班人员
                        String strAdd = "";
                        for (String memberNew : membersNew) {
                            if (!membersOld.contains(memberNew)){
                                if (strAdd != ""){
                                    strAdd += ",";
                                }
                                strAdd += memberNew;
                            }
                        }
                        //添加值班详细表
                        boolean flagDetail = dutyRecordDetailService.addDutyRecordDetail(dutyRecord.getTime(),strAdd,"2");

                        //找出删除的值班人员
                        String strDel = "";
                        for (String old : membersOld) {
                            if (!membersNew.contains(old)){
                                if (strDel != "")
                                {
                                    strDel += ",";
                                }
                                strDel += old;
                            }
                        }
                        //更新值班详细表
                        boolean flagUpdate = dutyRecordDetailService.updateDutyRecordDetail(dutyRecord.getTime(),strDel,"");
                        if (!flagDetail || !flagUpdate)
                        {
                            return RestResponse.failure("添加值班详细表失败");
                        }
                    }

                    //自动更新值班记录详细表
                    boolean flagDetail = dutyRecordDetailService.updateDutyRecordDetail();
                    if (!flagDetail){
                        return RestResponse.failure("更新值班记录详细表失败");
                    }

                }
            }else {
                return RestResponse.failure("今日无值班记录");
            }
            //修改防汛抗旱工作
            DutyFxkh dutyFxkh = new DutyFxkh();
            dutyFxkh.setTime(DateUtil.parse(today, DatePattern.NORM_DATE_PATTERN));
            dutyFxkh.setUpdateUser(user.getuserName());
            //查询是否有防汛抗旱工作
            List<DutyFxkh> fxkhList = dutyRecordService.getFxkhByTime(today);
            if (fxkhList.size() > 0){
                for (DutyFxkh fxkh : fxkhList) {
                    dutyFxkh.setId(fxkh.getId());
                    dutyFxkh.setWaterMain(fxkh.getRainMain());
                }
            }
            dutyRecordService.saveDutyFxkh(dutyFxkh);

            return RestResponse.success().setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/dutyExchange")
    @ApiOperation("换班")
    public RestResponse dutyExchange(@RequestParam("members") String members,
                                     @RequestParam("exchangeVal") String exchangeVal,
                                     @RequestParam("type") String type,
                                     @RequestParam("today") String today ){

        try {
            int res = 0;
            AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            List<DutyRecord> recordList = dutyRecordUserService.getDutyRecordToday(today,user.getAddvcd());
            if (recordList.size() == 0){
                return RestResponse.failure("今日无值班记录");
            }
            //最新值班人员
            List<String> membersOld = new ArrayList<>();
            membersOld = Arrays.asList(members.split(","));
            List<String> membersNew = new ArrayList<>();
            for (String old : membersOld) {
                if (!old.equals(exchangeVal)){
                    membersNew.add(old);
                    membersNew.add(user.getrealName());
                }
            }
            String newMember = StringUtils.join(membersNew.toArray(),",");

            DutyRecord dutyRecord = new DutyRecord();
            for (DutyRecord record : recordList) {
                dutyRecord.setLeader(record.getLeader());
                dutyRecord.setLeaderComm(record.getLeaderComm());
                dutyRecord.setTime(record.getTime());
                dutyRecord.setId(record.getId());
                if (type.equals("1")){
                    dutyRecord.setMember(exchangeVal);
                }else {
                    dutyRecord.setMember(newMember);
                }
                dutyRecord.setComments(record.getComments());
                dutyRecord.setUpdateUser(user.getuserName());
                dutyRecord.setAddvcd(user.getAddvcd());
            }
            res = dutyRecordService.saveDutyRecord(dutyRecord);
            //添加日志
            if(res > 0){
                //更新值班详细记录表
                if(!type.equals("1")){
                    boolean flagAdd = dutyRecordDetailService.addDutyRecordDetail(dutyRecord.getTime(),user.getrealName(),"1");
                    boolean flagDel = dutyRecordDetailService.updateDutyRecordDetail(dutyRecord.getTime(),exchangeVal,"1");
                    if (!flagAdd || !flagDel){
                        return RestResponse.failure("更新值班记录详细表失败");
                    }
                }

                String detail = "调班 原值班人员：" + members + "  现值班人员：" + (type == "1" ? exchangeVal : newMember);
                Log log = new Log();
                log.setTableName("Duty_Record");
                log.setTabaleId(res);
                log.setType("3");
                log.setUpdateUser(user.getuserName());
                log.setContent(detail);
                log.setAddvcd(user.getAddvcd());
                logService.insertLog(log);
                return RestResponse.success().setData(res);
            }else {
                return RestResponse.failure("调班失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }

    }

    @PostMapping("/getTypeWorkNew")
    @ApiOperation("获取最新交接班的标志")
    public RestResponse getTypeWorkNew(@RequestParam("id") String id){
        try {
            int res = 0;
            AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            DutyRecord dutyRecord = dutyRecordService.getDutyRecordById(id);
            if (dutyRecord == null){
                return RestResponse.failure("无值班记录");
            }
            String member = dutyRecord.getMember();

            if (!member.contains(user.getrealName())){
                res = 1;
            }else {
                if (dutyRecord.getDuty().equals("1")){
                    res = 4;
                }else {
                    if (member.split(",")[0].equals(user.getrealName())){
                        res = 2;
                    }else {
                        res = 3;
                    }
                }
            }
            return RestResponse.success().setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyRecordTelById")
    @ApiOperation("根据ID获取来电记录")
    public RestResponse getDutyRecordTelById(@RequestParam("id") String id){
        try {
            DutyRecordTel dutyRecordTel = dutyRecordService.getDutyRecordTelById(id);
            return RestResponse.success().setData(dutyRecordTel);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

}
