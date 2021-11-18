package com.sd.modules.system.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.common.collect.Maps;
import com.sd.annotation.SysLog;
import com.sd.modules.system.entity.User;
import com.sd.modules.system.service.DutyRecordDetailService;
import com.sd.modules.system.service.DutyRecordService;
import com.sd.modules.system.service.DutyRecordUserService;
import com.sd.modules.system.service.UserService;
import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordTel;
import com.sd.realm.AuthRealm.ShiroUser;
import com.sd.util.RestResponse;

import com.sd.util.ToolUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Chen Hualiang
 * @create 2020-10-10 10:13
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "登录相关")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Value("${server.port}")
    private String port;
    @Autowired
    private DutyRecordUserService dutyRecordUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private DutyRecordService dutyRecordService;

    @GetMapping(value = "/login",produces = {"application/json;charset=UTF-8"})
    @SysLog("用户登录")
    @ApiOperation("用户登录")
    public RestResponse loginMain(HttpServletRequest request){
        LOGGER.info("用户登录-----login/main:0");
        //用户名称
        String username = request.getParameter("username");
        //用户登录密码
        String password = request.getParameter("password");

        Map<String,Object> map = Maps.newHashMap();
        String error = null;
        //判断用户名或密码是否为空
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return RestResponse.failure("用户名或密码不能为空");
        }
        HttpSession session = request.getSession();
        if(null==session){
            return RestResponse.failure("session超时");
        }

        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);//获得token
        try {
            user.login(token);
            if(user.isAuthenticated()){//是否认证

                ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
                int userDuty = shiroUser.getUserDuty();

                String _duty_role_db = shiroUser.getRoleId().equals("2") ? "1" : "";
                String addvcd = shiroUser.getAddvcd();//行政区划码
                map.put("addvcd",addvcd);
                map.put("user",user.getPrincipal());
                map.put("token",user.getSession().getId());
                map.put("duty_time_today", ToolUtil.getToday());
                map.put("duty_role_db",_duty_role_db);
            }
        }catch (IncorrectCredentialsException e){
            error = "登录密码错误.";
        }catch (ExcessiveAttemptsException e){
            error = "登录失败次数过多";
        }catch (UnknownAccountException e){
//            error = "帐号不存在";
            error = e.getMessage();
        }catch (UnauthorizedException e){
            error = "您没有得到相应的授权！";
        }

        if(StringUtils.isBlank(error)){
            return RestResponse.success("登录成功").setData(map);
        }else {
            return RestResponse.failure(error).setData(map);
        }

    }

    @GetMapping("/systemLogout")
    @SysLog("退出系统")
    @ApiOperation("用户退出系统")
    public RestResponse systemlogOut(){
        LOGGER.info("用户退出系统:");
        SecurityUtils.getSubject().logout();
        return RestResponse.success("退出成功").setCode(0);
    }

    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public RestResponse getUserInfo(){
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        return RestResponse.success("获取成功").setData(user);
    }

    @GetMapping("/getDutyToday")
    public RestResponse getDutyToday(){
        //今日值班
        String dutyInfo = "";
        String _duty = "";
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyRecord> list = dutyRecordUserService.getDutyRecordToday(ToolUtil.getToday(),addvcd);
        if (list.size() > 0){
            dutyInfo = list.get(0).getMember().trim();
            _duty = list.get(0).getDuty();
        }else {
            dutyInfo = "无";
        }
        String dutyToday = "今日值班：" + dutyInfo;
        if (StringUtils.isNotBlank(_duty)){
            dutyToday += (_duty.equals("1") ? "" : "（未接班）");
        }else {
            dutyToday += "（未接班）";
        }
        return RestResponse.success().setData(dutyToday);
    }

    @GetMapping("/getDuty")
    public RestResponse getDuty(){
        //今日值班 值班首页使用 只查询省级排班情况
        String dutyMember = "";
        String dutyLeader = "";
        String dutyLeaderComm = "";
//        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
//        String addvcd = user.getAddvcd();
        String addvcd = "37";
        List<DutyRecord> list = dutyRecordUserService.getDutyRecordToday(ToolUtil.getToday(),addvcd);
        Map<String,Object> map = new HashMap<>();
        if (list.size() > 0){
            dutyMember = list.get(0).getMember().trim();
            dutyLeader = list.get(0).getLeader().trim();
            dutyLeaderComm = list.get(0).getLeaderComm().trim();
            map.put("leaderComm",dutyLeaderComm);
            map.put("leader",dutyLeader);
            map.put("member",dutyMember);
            return RestResponse.success().setData(map);
        }else {
            return RestResponse.failure("今日未排班");
        }
    }

    @PostMapping("/getDutyForDp")
    public RestResponse getDutyForDp(@RequestParam("addvcd") String addvcd){
        try {
            String dutyMember = "";
            String dutyLeader = "";
            String dutyLeaderComm = "";
            if (addvcd.equals("")){ //默认addvcd为空时查询省局信息
                addvcd = "37";
            }
            List<DutyRecord> list = dutyRecordUserService.getDutyRecordToday(ToolUtil.getToday(),addvcd);

            Map<String,Object> map = new HashMap<>();
            if (list.size() > 0){
                dutyMember = list.get(0).getMember().trim();
                dutyLeader = list.get(0).getLeader().trim();
                dutyLeaderComm = list.get(0).getLeaderComm().trim();
                //局领导
                map.put("leaderComm",dutyLeaderComm);
                //带班
                map.put("leader",dutyLeader);
                //值班人员
                map.put("member",dutyMember);

                String[] members = dutyMember.split(",");
                //主班
                map.put("main",members[0]);
                //副班
                map.put("deputy",members[1]);

                User leader = userService.getUserByName(dutyLeader,addvcd);
                map.put("leaderTel",leader.getUserTel());
                User mainDuty = userService.getUserByName(members[0],addvcd);
                map.put("mainTel",mainDuty.getUserTel());
                User deputyDuty = userService.getUserByName(members[1],addvcd);
                map.put("deputyTel",deputyDuty.getUserTel());
                return RestResponse.success().setData(map);
            }else {
                return RestResponse.failure("今日未排班");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getTelFile")
    public RestResponse getTelFile(@RequestParam("addvcd") String addvcd,@RequestParam("date") String date,@RequestParam("file") MultipartFile file){
        try {
            DutyRecordTel dutyRecordTel = dutyRecordService.getTelFile(addvcd,date,file);
            return RestResponse.success("保存来电记录成功！").setData(dutyRecordTel);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }
}
