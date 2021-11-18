package com.sd.modules.system.controller;

import com.sd.base.MySysUser;
import com.sd.modules.system.entity.User;
import com.sd.modules.system.service.DutyFileService;
import com.sd.modules.system.service.DutyRoleService;
import com.sd.modules.system.service.UserService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.DutyDict;
import com.sd.pojo.DutyRole;
import com.sd.util.RestResponse;
import com.sd.util.ToolUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Chen Hualiang
 * @create 2020-10-14 10:30
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
@Api(tags = "用户相关")
public class UserController {

    @Autowired
    protected UserService userService;
    @Autowired
    protected DutyFileService dutyFileService;
    @Autowired
    protected DutyRoleService dutyRoleService;


    /**
     * 修改用户密码
     * @param newPwd
     * @param confirmPwd
     * @return
     */
//    @RequiresPermissions("sys:user:changePassword")
    @PostMapping("/changePassword")
    @ApiOperation("修改用户密码")
    public RestResponse changePassword(@RequestParam(value = "newPwd",required = false)String newPwd,
                                       @RequestParam(value = "confirmPwd",required = false)String confirmPwd){
        if(StringUtils.isBlank(newPwd)){
            return RestResponse.failure("新密码不能为空");
        }

        if(StringUtils.isBlank(confirmPwd)){
            return RestResponse.failure("确认密码不能为空");
        }

        if(!confirmPwd.equals(newPwd)){
            return RestResponse.failure("两次输入密码不一致");
        }
        //获取用户信息
        User user = userService.findUserById(MySysUser.id());
        //设置新密码
        user.setPassword(newPwd);
        ToolUtil.entryptPassword(user);
        userService.updateUser(user);
        return RestResponse.success();
    }

    @PostMapping("/getDutyUser")
    @ApiOperation("获取登录人员列表")
    public RestResponse getDutyUser(@RequestBody List<TableData> list){
        try {
            int sEcho = 0;
            for (TableData tableData : list) {
                if (tableData.getName().equals("sEcho")){
                    sEcho = Integer.parseInt(tableData.getValue());
                }
            }
//            List<String> returnList = new ArrayList<>();
            List<User> userList = userService.getDutyUser();
            List<DutyDict> dicts = dutyFileService.getDutyDict("0102");
            List<Map<String,Object>> dic = new ArrayList<>();
            for (User user : userList) {
                Integer userId = user.getUserid();
                DutyRole dutyRole = dutyRoleService.findRoleByUserId(userId);
                if(dutyRole != null){
                    String roleId = dutyRole.getRoleId();
                    String userType = dutyRole.getUserType();
                    int sort = dutyRole.getSort();

                    Map<String,Object> map = new HashMap<>();
                    map.put("userid",user.getUserid());
                    map.put("userName",user.getUserName());
                    map.put("realName",user.getRealName());
                    map.put("phone",user.getUserTel());
//                map.put("email",user.getEmail());
                    String dictName = getDictNameMuti(dicts,userType);
                    map.put("userType",dictName);
                    map.put("sort",sort);
//                String role = user.getRoleId();
                    if (roleId.equals("1")){
                        roleId = "管理员";
                    }
                    if (roleId.equals("2")){
                        roleId = "带班领导";
                    }
                    if (roleId.equals("3")){
                        roleId = "值班人员";
                    }
                    map.put("role",roleId);
                    dic.add(map);
                }else{//已开通值班权限，但未分配人员角色，默认为普通值班人员，向角色权限表插数据
                    DutyRole adminDutyRole = new DutyRole();
                    adminDutyRole.setUserId(userId);
                    adminDutyRole.setRoleId("3");
                    adminDutyRole.setUserType("3");
                    adminDutyRole.setAddvcd(user.getAddvcd());
                    dutyRoleService.saveDutyRole(adminDutyRole);

                    Map<String,Object> map = new HashMap<>();
                    map.put("userid",userId);
                    map.put("userName",user.getUserName());
                    map.put("realName",user.getRealName());
                    map.put("phone",user.getUserTel());
                    map.put("userType","值班人员");
                    map.put("role","值班人员");
                    dic.add(map);
//                    return RestResponse.failure("权限表有未分配值班人员角色权限用户，用户id为：" + userId);
                }

            }
            int total = userService.getDutyUserCount();

            DutyUserListVO model = new DutyUserListVO();
            model.setDraw(sEcho);
            model.setRecordsTotal(total);
            model.setRecordsFiltered(total);
            model.setData(dic);

            return RestResponse.success().setData(model);
        }catch (Exception e){
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/isExistUser")
    @ApiOperation("验证用户名是否重复")
    public RestResponse isExistUser(@RequestParam("userName") String userName){
        try {
            boolean isExist = userService.isExistUser(userName);
            return RestResponse.success().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }
    /**
     * 新建用户
     * @param user
     * @return
     */
    @PostMapping("/saveDutyUser")
    @ApiOperation("新增或修改用户")
    public RestResponse saveDutyUser(@RequestBody User user){
        try {
            long roleRes = 0;
            User saveUser = new User();
            saveUser.setUserid(user.getUserid());
            saveUser.setUserName(user.getUserName());
            saveUser.setRealName(user.getRealName());
            saveUser.setPassword(user.getPassword());
            saveUser.setAddvcd(user.getAddvcd());
            saveUser.setUserTel(user.getUserTel());
            saveUser.setUserWeb(1);
            saveUser.setRoleWeb(1);
            saveUser.setRoleAdmin(7);
            saveUser.setUserPhone(0);
            saveUser.setUserPhoneShow(0);
            saveUser.setUserSms(0);
            saveUser.setUserDuty(1);
            //密码加密
            ToolUtil.entryptPassword(saveUser);
            long res = userService.saveDutyUser(saveUser);
            if (res > 0){
                DutyRole dutyRole = new DutyRole();
                if (user.getUserid() == null){//新增用户
                    dutyRole.setUserId(saveUser.getUserid());
                    dutyRole.setRoleId(user.getRoleId());
                    dutyRole.setUserType(user.getUserType());
                    dutyRole.setSort(user.getSort());
                    dutyRole.setAddvcd(saveUser.getAddvcd());
                    roleRes = dutyRoleService.saveDutyRole(dutyRole);
                }else {//修改用户
                    dutyRole.setUserId(user.getUserid());
                    dutyRole.setRoleId(user.getRoleId());
                    dutyRole.setUserType(user.getUserType());
                    dutyRole.setSort(user.getSort());
                    dutyRole.setAddvcd(saveUser.getAddvcd());
                    roleRes = dutyRoleService.updateDutyRole(dutyRole);
                }

                if (roleRes > 0){
                    return RestResponse.success().setData(roleRes);
                }else {
                    return RestResponse.failure("新增用户失败");
                }
            }else {
                return RestResponse.failure("新增用户失败");
            }

//            saveUser.setEmail(user.getEmail());
//            saveUser.setRoleId(user.getRoleId());
//            saveUser.setUserType(user.getUserType());
//            saveUser.setSort(user.getSort());


//            return RestResponse.success().setData(res);
        }catch (Exception e){
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delDutyUser")
    @ApiOperation("删除用户")
    public RestResponse delDutyUser(@RequestParam("userid") String userid){
        try {
            //删除用户改为将user_duty标识改为0，去掉值班系统的权限,不是直接从表删除用户
            boolean delflag = userService.delUser(userid);
            return RestResponse.success().setData(delflag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyUserInfo")
    @ApiOperation("获取值班用户信息")
    public RestResponse getDutyUserInfo(@RequestParam("userid") String userid){
        try {
            int id = Integer.parseInt(userid);
//            int id = Long.parseLong(userid);
            User user = userService.findUserById(id);
            return RestResponse.success().setData(user);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyUserSort")
    @ApiOperation("根据人员类型获取最新排序")
    public RestResponse getDutyUserSort(@RequestParam("userType") String userType){
        try {

            int sort = dutyRoleService.getDutyUserSort(userType);
//            int sort = userService.getDutyUserSort(userType);
            return RestResponse.success().setData(sort);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/initPassword")
    @ApiOperation("初始化密码")
    public RestResponse initPassword(@RequestParam("userid")String userid,@RequestParam("password") String password){
        try {
            User user = userService.findUserById(Integer.parseInt(userid));
            user.setPassword(password);
            ToolUtil.entryptPassword(user);
            User outUser = userService.updateUser(user);
            if (outUser != null){
                return RestResponse.success().setData(true);
            }else {
                return RestResponse.failure("初始化密码失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getUserInfoByName")
    @ApiOperation("根据用户名获取用户信息")
    public RestResponse getUserInfoByName(@RequestParam("userName") String userName){
        try {
            User user = userService.findUserByUserName(userName);
            return RestResponse.success().setData(user);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    /**
     * 根据代码获取名称(多选)-列表用
     * @param dicts
     * @param dabh
     * @return
     */
    public String getDictNameMuti(List<DutyDict> dicts,String dabh){
        String res = "";
        if (dicts.size() == 0){
            return res;
        }
        if (dabh.equals("")){
            return res;
        }
        List<String> strs = Arrays.asList(dabh.split(","));
        for (String str : strs) {
            for (DutyDict dict : dicts) {
                if (dict.getDabh().equals(str)){
                    if (!res.equals("")){
                        res += ",";
                    }
                    res += dict.getDaxx().trim();
                    break;
                }
            }
        }
        return res;
    }
}
