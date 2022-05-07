package com.sd.modules.system.controller;

import com.sd.modules.system.service.CommunicationNewService;
import com.sd.modules.system.service.CommunicationService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.AddvcdStcd;
import com.sd.pojo.Communication;
import com.sd.realm.AuthRealm;
import com.sd.util.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Api(tags = "通讯录管理(新)")
public class CommunicationNewController {
    @Autowired
    private CommunicationNewService communicationNewService;
    @Autowired
    private CommunicationService communicationService;

    @GetMapping("/getAddvcdStcd")
    @ApiOperation("获取市及水文中心")
    public RestResponse getAddvcdStcd(){
        try{
            List<AddvcdStcd> list = communicationNewService.getAddvcdStcd();
            return  RestResponse.success().setData(list);
        }catch (Exception e){
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/getCommunicationNew")
    @ApiOperation("获取通讯录列表")
    public RestResponse getCommunicationNew(@RequestParam("addvcd") String addvcd){
        try {
            List<Communication> communications = communicationNewService.getCommunicationNew(addvcd);

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
//            int total = communicationService.getCommunicationCount();
//            dic.add(total, (Map<String, Object>) new HashMap<String, Object>().put("total",total));
            return  RestResponse.success().setData(dic);
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
            saveComm.setAddvcd(communication.getAddvcd());
            int res = communicationService.saveCommunication(saveComm);

            return RestResponse.success().setData(res);
        }catch (Exception e){
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }
}
