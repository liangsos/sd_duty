package com.sd.modules.system.controller;

import com.sd.modules.system.service.CommunicationService;
import com.sd.modules.system.service.PhoneService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.DutyUserModel;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.Communication;
import com.sd.pojo.DutyRecord;
import com.sd.util.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2021-06-29 12:25
 */
@RestController
@RequestMapping("/api/phone")
@CrossOrigin
@Api(tags = "手机端接口管理")
public class PhoneController {
    @Autowired
    private PhoneService phoneService;

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
            List<Communication> communications = phoneService.getCommunication();

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
            int total = phoneService.getCommunicationCount();

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

    @PostMapping("/getDutyBb")
    @ApiOperation("值班记录查询")
    public RestResponse getDutyBb(@RequestBody List<TableData> list){
        try {
            int sEcho = 0;
            int iDisplayStart = 0;
            int iDisplayLength = 0;
            //查询时间
            String search_begin_time = "";
            String search_end_time = "";

            for (TableData tableData : list) {
                if (tableData.getName().equals("sEcho")){
                    sEcho = Integer.parseInt(tableData.getValue());
                }
                if (tableData.getName().equals("iDisplayStart")){
                    iDisplayStart = Integer.parseInt(tableData.getValue());
                }
                if (tableData.getName().equals("iDisplayLength")){
                    iDisplayLength = Integer.parseInt(tableData.getValue());
                }
                if (tableData.getName().equals("search_begin_time")){
                    search_begin_time = tableData.getValue();
                }
                if (tableData.getName().equals("search_end_time")){
                    search_end_time = tableData.getValue();
                }
            }

            List<DutyRecord> dutyRecordList = phoneService.getDutyBb(search_begin_time,search_end_time,iDisplayStart,iDisplayStart + iDisplayLength);

            for (DutyRecord dutyRecord : dutyRecordList) {
                if (StringUtils.isBlank(dutyRecord.getDuty())){
                    dutyRecord.setDuty("否");
                }else {
                    dutyRecord.setDuty(dutyRecord.getDuty().equals("1") ? "是" : "否");
                }
            }

            int count = phoneService.getDutyBbCount(search_begin_time,search_end_time);

            DutyUserModel model = new DutyUserModel();
            model.setDraw(sEcho);
            model.setRecordsTotal(count);
            model.setRecordsFiltered(count);
            model.setData(dutyRecordList);

            return RestResponse.success().setData(model);

        } catch (Exception e) {
            return RestResponse.failure(e.getMessage());
        }

    }
}
