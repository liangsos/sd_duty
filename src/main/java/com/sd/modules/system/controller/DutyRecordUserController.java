package com.sd.modules.system.controller;

import com.sd.modules.system.entity.Log;
import com.sd.modules.system.entity.User;
import com.sd.modules.system.service.*;
import com.sd.modules.system.vo.DutyRecordPlanVO;
import com.sd.modules.system.vo.DutyRecordVO;
import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordPlan;
import com.sd.realm.AuthRealm;
import com.sd.util.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 排班管理
 * @author Chen Hualiang
 * @create 2020-10-19 9:31
 */
@RestController
@RequestMapping("/api/dutyRecordUser")
@CrossOrigin
@Api(tags = "排班管理")
public class DutyRecordUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");

    @Autowired
    protected DutyRecordUserService dutyRecordUserService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected LogService logService;
    @Autowired
    protected DutyRecordDetailService dutyRecordDetailService;
    @Autowired
    protected IImportExcelService iImportExcelService;
    @Autowired
    protected DutyRecordPlanService dutyRecordPlanService;
    @Autowired
    protected DutyRecordService dutyRecordService;
    @Autowired
    protected ExportExcelService exportExcelService;

    @PostMapping("/getDutyRecordPlan")
    public RestResponse getDutyRecordPlan(@RequestParam("beginTime")String beginTime,@RequestParam("endTime") String endTime){
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyRecordPlan> list = dutyRecordUserService.getDutyRecordPlan(beginTime,endTime,addvcd);
        List<DutyRecordPlanVO> planList = new ArrayList<>();
        for (DutyRecordPlan dutyRecordPlan : list) {
            DutyRecordPlanVO recordPlanVO = new DutyRecordPlanVO();
            recordPlanVO.setId(String.valueOf(dutyRecordPlan.getId()));
            recordPlanVO.setStart(sf.format(dutyRecordPlan.getTime()));
            recordPlanVO.setTitle("");
            recordPlanVO.setUrl("");
            recordPlanVO.setClassName("fc-cell-new");
            recordPlanVO.setAllDay(true);
            recordPlanVO.setEditable(false);
            recordPlanVO.setLeaderComm(dutyRecordPlan.getLeaderComm());
            recordPlanVO.setLeader(dutyRecordPlan.getLeader());
            recordPlanVO.setMember(dutyRecordPlan.getMember());
            planList.add(recordPlanVO);
        }
        return RestResponse.success().setData(planList);
    }

    @PostMapping("/getDutyRecord")
    @ApiOperation("获取值班记录")
    public RestResponse getDutyRecord(@RequestParam("beginTime")String beginTime,@RequestParam("endTime") String endTime){
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyRecord> list = dutyRecordUserService.getDutyRecord(beginTime,endTime,addvcd);
        List<DutyRecordVO> dutyList = new ArrayList<>();
        for (DutyRecord dutyRecord : list) {
            DutyRecordVO recordVO = new DutyRecordVO();
            recordVO.setId(String.valueOf(dutyRecord.getId()));
            recordVO.setStart(sf.format(dutyRecord.getTime()));
            recordVO.setTitle("");
            recordVO.setUrl("");
            recordVO.setClassName("fc-cell-new");
            recordVO.setAllDay(true);
            recordVO.setEditable(false);
            recordVO.setLeaderComm(dutyRecord.getLeaderComm());
            recordVO.setLeader(dutyRecord.getLeader());
            recordVO.setMember(dutyRecord.getMember());
            recordVO.setComments(dutyRecord.getComments());
            recordVO.setDuty(dutyRecord.getDuty());
            dutyList.add(recordVO);
        }
        return RestResponse.success().setData(dutyList);
    }

    @PostMapping("/getUserByType")
    @ApiOperation("根据类型获取人员")
    public RestResponse getUserByType(@RequestParam("type") String type,@RequestParam("addvcd") String addvcd){
        List<User> list = userService.getUserByType(type,addvcd);
        if (list != null){
            return RestResponse.success().setData(list);
        }else {
            return RestResponse.failure("此人员类型无用户数据");
        }
    }

    @PostMapping("/getDutyRecordToday")
    @ApiOperation("获取今日值班记录")
    public RestResponse getDutyRecordToday(@RequestParam("time")String time){
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyRecord> list = dutyRecordUserService.getDutyRecordToday(time,addvcd);
        List<DutyRecordVO> dutyList = new ArrayList<>();
        for (DutyRecord dutyRecord : list) {
            DutyRecordVO recordVO = new DutyRecordVO();
            recordVO.setId(String.valueOf(dutyRecord.getId()));
            recordVO.setStart(sf.format(dutyRecord.getTime()));
            recordVO.setTitle("");
            recordVO.setUrl("");
            recordVO.setClassName("fc-cell-new");
            recordVO.setAllDay(true);
            recordVO.setEditable(false);
            recordVO.setLeaderComm(dutyRecord.getLeaderComm());
            recordVO.setLeader(dutyRecord.getLeader());
            recordVO.setMember(dutyRecord.getMember());
            recordVO.setComments(dutyRecord.getComments());
            recordVO.setDuty(dutyRecord.getDuty());
            dutyList.add(recordVO);
        }
        return RestResponse.success().setData(dutyList);
    }

    @PostMapping("/saveDutyRecord")
    @ApiOperation("保存值班记录")
    public RestResponse saveDutyRecord(@RequestBody DutyRecord dutyRecord){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String id = String.valueOf(dutyRecord.getId());
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        //获取旧的值班记录
        List<DutyRecord> recordOldList = new ArrayList<>();
        if (!id.equals("null")){
            recordOldList = dutyRecordUserService.getDutyRecordById(id);
            if (recordOldList.size() == 0){
                return RestResponse.failure("当天值班记录不存在！");
            }
        }

        //保存值班记录
        int res = dutyRecordUserService.saveDutyRecord(dutyRecord);
        if (res > 0){
            //添加日志
            if (id.equals("null")){//新增
                String detail = "日期:" + df.format(dutyRecord.getTime()) + "局领导:" + dutyRecord.getLeaderComm() + "带班:" +
                        dutyRecord.getLeader() + "值班人员:" + dutyRecord.getMember() + "备注:" + dutyRecord.getComments();

                Log log = new Log();
                log.setTableName("Duty_Record");
                log.setTabaleId(res);
                log.setType("1");
                log.setContent(detail);
                log.setUpdateUser(user.getuserName());
                log.setAddvcd(addvcd);
                int logRes = logService.insertLog(log);

                //添加值班详细表
                boolean flagDetail = dutyRecordDetailService.addDutyRecordDetail(dutyRecord.getTime(),dutyRecord.getMember(),"1");

                if (!flagDetail){
                    return RestResponse.failure("添加值班详细表失败");
                }

            }else {//存在记录时更新
                String detail = "";
                String oldLeaderComm = recordOldList.get(0).getLeaderComm().trim();
                String oldLeader = recordOldList.get(0).getLeader();
                String oldMember = recordOldList.get(0).getMember();
                String oldComments = recordOldList.get(0).getComments();
                if (!dutyRecord.getLeaderComm().trim().equals(oldLeaderComm)){
                    detail += "局领导：旧((" + oldLeaderComm + ") 新(" + dutyRecord.getLeaderComm() + ")";
                }
                if (!dutyRecord.getLeader().trim().equals(oldLeader)){
                    detail += "带班：旧(" + oldLeader + ") 新(" + dutyRecord.getLeader() + ")";
                }
                if (!dutyRecord.getMember().trim().equals(oldMember)){
                    detail += "值班人员：旧(" + oldMember + ") 新(" + dutyRecord.getMember() + ")";
                }
                if (!dutyRecord.getComments().trim().equals(oldComments)){
                    detail += "备注：旧(" + oldComments + ") 新(" + dutyRecord.getComments() + ")";
                }
                Log log = new Log();
                log.setTableName("Duty_Record");
                log.setTabaleId(dutyRecord.getId());
                log.setType("3");
                log.setContent(detail);
                log.setUpdateUser(user.getuserName());
                log.setAddvcd(addvcd);
                int logRes = logService.insertLog(log);

                //值班人员有变动时更新值班详情表
                if(!dutyRecord.getMember().equals(oldMember)){
                    List<String> membersOld = new ArrayList<>();
                    List<String> membersNew = new ArrayList<>();
                    membersOld = Arrays.asList(oldMember.split(","));
                    membersNew = Arrays.asList(dutyRecord.getMember().split(","));
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
                    for (String memberOld : membersOld) {
                        if (!membersNew.contains(memberOld)){
                            if (strDel != "")
                            {
                                strDel += ",";
                            }
                            strDel += memberOld;
                        }
                    }
                    //更新值班详细表
                    boolean flagUpdate = dutyRecordDetailService.updateDutyRecordDetail(dutyRecord.getTime(),strDel,"");
                    if (!flagDetail || !flagUpdate)
                    {
                        return RestResponse.failure("添加值班详细表失败");
                    }
                }
            }
        }

        return RestResponse.success("更新成功！").setData(res);
    }

    @PostMapping("/getDutyTj")
    @ApiOperation("获取值班统计")
    public RestResponse getDutyTj(@RequestParam("beginTime")String beginTime,@RequestParam("endTime") String endTime){
        //先更新值班详细表
        try {
            boolean flag= dutyRecordDetailService.updateDutyRecordDetail();
            List<Map<String,Object>> list = dutyRecordUserService.getDutyTj(beginTime,endTime);
            return RestResponse.success().setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/uploadExcel")
    @ApiOperation("批量导入排班")
    public RestResponse uploadExcel(MultipartFile file, HttpServletRequest req, HttpServletResponse resp){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<DutyRecordPlan> list = iImportExcelService.importExcel(file, req, resp);
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if(list == null || list.size() == 0 ) {
            return RestResponse.failure("导入Excel失败！");
        }

        for(DutyRecordPlan plan:list) {
            DutyRecordPlan dutyRecordPlan = new DutyRecordPlan();
            System.out.println(plan.toString());
            //更新排班表
            //获取当天排班情况
            DutyRecordPlan recordPlan = dutyRecordPlanService.getDutyRecordPlanToday(plan.getTime(),user.getAddvcd());
            if (recordPlan != null){
                dutyRecordPlan.setId(recordPlan.getId());
            }
            dutyRecordPlan.setTime(plan.getTime());
            dutyRecordPlan.setLeaderComm(plan.getLeaderComm());
            dutyRecordPlan.setLeader(plan.getLeader());
            dutyRecordPlan.setMember(plan.getMember());
            dutyRecordPlan.setUpdateUser(user.getuserName());
            dutyRecordPlan.setAddvcd(user.getAddvcd());
            int tempPlan = dutyRecordPlanService.saveDutyPlan(dutyRecordPlan);
            if (tempPlan <= 0){
                return RestResponse.failure("0;保存值班表失败！批量保存终止。");
            }

            //更新值班表
            DutyRecord dutyRecord = new DutyRecord();
            //获取当天值班情况
            DutyRecord recordToday = dutyRecordService.getDutyRecordToday(plan.getTime(),user.getAddvcd());
            if (recordToday != null){//存在记录
                dutyRecord.setId(recordToday.getId());
                dutyRecord.setComments(recordToday.getComments());
            }
            dutyRecord.setTime(plan.getTime());
            dutyRecord.setLeaderComm(plan.getLeaderComm());
            dutyRecord.setLeader(plan.getLeader());
            dutyRecord.setMember(plan.getMember());
            dutyRecord.setUpdateUser(user.getuserName());
            dutyRecord.setAddvcd(user.getAddvcd());
            int temp = dutyRecordService.saveDutyRecord(dutyRecord);
            if (temp > 0){
                //添加日志
                String detail = "";
                if (recordToday != null){//存在记录
                    detail = "批量导入修改 日期：" + df.format(plan.getTime());
                    if (!dutyRecord.getLeaderComm().trim().equals(recordToday.getLeaderComm().trim())){
                        detail += " 局领导：旧(" + recordToday.getLeaderComm() +  ") 新(" + dutyRecord.getLeaderComm() + ")";
                    }
                    if (!dutyRecord.getLeader().trim().equals(recordToday.getLeader().trim())){
                        detail += " 带班：旧(" + recordToday.getLeader() + ") 新(" + dutyRecord.getLeader() + ")";
                    }
                    if (!dutyRecord.getMember().trim().equals(recordToday.getMember().trim())){
                        detail += " 值班人员：旧(" + recordToday.getMember() + ") 新(" + dutyRecord.getMember() + ")";
                    }
                    Log log = new Log();
                    log.setTableName("Duty_Record");
                    log.setTabaleId(temp);
                    log.setType("3");
                    log.setContent(detail);
                    log.setUpdateUser(user.getuserName());
                    log.setAddvcd(user.getAddvcd());
                    int flag = logService.insertLog(log);

                    //值班人员有变动时更新值班详细表
                    if (!dutyRecord.getMember().trim().equals(recordToday.getMember().trim())){
                        String oldMember = recordToday.getMember().trim();
                        List<String> membersOld = new ArrayList<>();
                        List<String> membersNew = new ArrayList<>();
                        membersOld = Arrays.asList(oldMember.split(","));
                        membersNew = Arrays.asList(dutyRecord.getMember().split(","));
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
                        for (String memberOld : membersOld) {
                            if (!membersNew.contains(memberOld)){
                                if (strDel != "")
                                {
                                    strDel += ",";
                                }
                                strDel += memberOld;
                            }
                        }
                        //更新值班详细表
                        boolean flagUpdate = dutyRecordDetailService.updateDutyRecordDetail(dutyRecord.getTime(),strDel,"");
                        if (!flagDetail || !flagUpdate)
                        {
                            return RestResponse.failure("0;添加值班详细表失败");
                        }
                    }
                }else {//不存在记录 直接插入数据
                    detail = "批量导入 日期：" + df.format(plan.getTime()) + " 局领导：" + dutyRecord.getLeaderComm() + " 带班：" + dutyRecord.getLeader()
                            + " 值班人员：" + dutyRecord.getMember();
                    Log log = new Log();
                    log.setTableName("Duty_Record");
                    log.setTabaleId(temp);
                    log.setType("1");
                    log.setContent(detail);
                    log.setUpdateUser(user.getuserName());
                    log.setAddvcd(user.getAddvcd());
                    int flag = logService.insertLog(log);

                    boolean flagDetail = dutyRecordDetailService.addDutyRecordDetail(dutyRecord.getTime(),dutyRecord.getMember(),"1");
                    if (!flagDetail){
                        return RestResponse.failure("0;保存值班记录详细表失败！批量保存终止！");
                    }
                }
            }else {
                return RestResponse.failure("0;保存值班记录失败！批量保存终止 ！");
            }
        }
        return RestResponse.success("1;批量导入成功");
    }

    @RequestMapping("/exportDutyExcel")
    @ApiOperation("导出值班表")
    public RestResponse exportDutyExcel(@RequestParam("beginTime")String beginTime,@RequestParam("endTime") String endTime){
        try {
            String url = exportExcelService.exportDutyExcel(beginTime,endTime);
            return RestResponse.success().setData(url);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }

    }
}
