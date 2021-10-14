package com.sd.modules.system.controller;

import com.sd.modules.system.service.CommunicationNewService;
import com.sd.modules.system.service.CommunicationService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.AddvcdStcd;
import com.sd.pojo.Communication;
import com.sd.util.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
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
    public RestResponse getCommunicationNew(){
        try {
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
            return  RestResponse.success().setData(dic);
        }catch (Exception e){
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }
}
