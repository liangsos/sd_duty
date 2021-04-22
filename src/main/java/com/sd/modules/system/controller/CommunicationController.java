package com.sd.modules.system.controller;

import com.sd.modules.system.entity.User;
import com.sd.modules.system.service.CommunicationService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.Communication;
import com.sd.pojo.DutyAddress;
import com.sd.pojo.DutyDict;
import com.sd.realm.AuthRealm;
import com.sd.util.RestResponse;
import com.sd.util.ToolUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2020-11-12 10:47
 */
@RestController
@RequestMapping("/api/communication")
@Api(tags = "通讯录管理")
public class CommunicationController {

    @Autowired
    private CommunicationService communicationService;

    @PostMapping("/getCommunication")
    @ApiOperation("获取通讯录列表")
    public RestResponse getCommunication(@RequestBody List<TableData> list){
        try {
            int sEcho = 0;
            for (TableData tableData : list) {
                if (tableData.getName().equals("sEcho")){
                    sEcho = Integer.parseInt(tableData.getValue());
                }
            }
//            List<String> returnList = new ArrayList<>();
            List<Communication> communications = communicationService.getCommunication();

            List<Map<String,Object>> dic = new ArrayList<>();
            for (Communication communication : communications) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",communication.getId());
                map.put("name",communication.getName());
                map.put("unit",communication.getUnit());
                map.put("position",communication.getPosition());
                map.put("tel",communication.getTel());
                map.put("phone",communication.getPhone());
                map.put("dutyTel",communication.getDutyTel());
                map.put("sort",communication.getSort());
                dic.add(map);
            }
            int total = communicationService.getCommunicationCount();

            DutyUserListVO model = new DutyUserListVO();
            model.setDraw(sEcho);
            model.setRecordsTotal(total);
            model.setRecordsFiltered(total);
            model.setData(dic);

            return  RestResponse.success().setData(model);
        }catch (Exception e){
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    /**
     * 新增或修改通讯录人员
     * @param communication
     * @return
     */
    @PostMapping("/saveCommunication")
    @ApiOperation("新增或修改通讯录人员")
    public RestResponse saveCommunication(@RequestBody Communication communication){
        try {
            AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            Communication saveComm = new Communication();
            saveComm.setId(communication.getId());
            saveComm.setName(communication.getName());
            saveComm.setUnit(communication.getUnit());
            saveComm.setPosition(communication.getPosition());
            saveComm.setTel(communication.getTel());
            saveComm.setPhone(communication.getPhone());
            saveComm.setDutyTel(communication.getDutyTel());
            saveComm.setSort(communication.getSort());
            saveComm.setUpdateUser(user.getuserName());
            saveComm.setAddvcd(user.getAddvcd());
            int res = communicationService.saveCommunication(saveComm);

            return RestResponse.success().setData(res);
        }catch (Exception e){
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delCommunication")
    @ApiOperation("删除通讯录人员")
    public  RestResponse delCommunication(@RequestParam("id") String id){
        try {
            boolean delflag = communicationService.delCommunication(id);
            return RestResponse.success().setData(delflag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getCommunicationById")
    @ApiOperation("获取通讯录人员信息")
    public  RestResponse getCommunicationById(@RequestParam("id") String id){
        try {
            Communication communication = communicationService.getCommunicationById(id);
            return RestResponse.success().setData(communication);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }
}
