package com.sd.modules.system.controller;

import com.sd.modules.system.service.DutyOrganService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.DutyOrgan;
import com.sd.util.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2020-10-26 18:45
 */
@RestController
@RequestMapping("/api/dutyOrgan")
@CrossOrigin
@Api(tags = "机构管理")
public class DutyOrganController {

    @Autowired
    protected DutyOrganService dutyOrganService;

    @PostMapping("/getDutyOrgan")
    @ApiOperation("获取机构列表")
    public RestResponse getDutyOrgan(@RequestBody List<TableData> list){
        try {
            int sEcho = 0;
            int iDisplayStart = 0;
            int iDisplayLength = 0;
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
            }

            List<DutyOrgan> organs = dutyOrganService.getDutyOrgan(iDisplayStart,iDisplayStart + iDisplayLength);
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyOrgan organ : organs) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",organ.getId());
                map.put("name",organ.getName());
                map.put("leader",organ.getLeader());
                map.put("comments",organ.getComments());
                dataList.add(map);
            }

            int total = dutyOrganService.getDutyOrganCount();
            DutyUserListVO model = new DutyUserListVO();
            model.setDraw(sEcho);
            model.setRecordsTotal(total);
            model.setRecordsFiltered(total);
            model.setData(dataList);

            return RestResponse.success().setData(model);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/checkOrgan")
    @ApiOperation("校验机构名称")
    public RestResponse checkOrgan(@RequestParam("name") String name){
        try {
            boolean isExist = dutyOrganService.checkOrgan(name);
            return RestResponse.success().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveDutyOrgan")
    @ApiOperation("新增或修改机构")
    public  RestResponse saveDutyOrgan(@RequestBody DutyOrgan dutyOrgan){
        try {
            DutyOrgan saveOrgan = new DutyOrgan();
            saveOrgan.setId(dutyOrgan.getId());
            saveOrgan.setLeader(dutyOrgan.getLeader());
            saveOrgan.setName(dutyOrgan.getName());
            saveOrgan.setComments(dutyOrgan.getComments());

            int res = dutyOrganService.saveDutyOrgan(saveOrgan);

            return RestResponse.success().setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delDutyOrgan")
    @ApiOperation("删除机构")
    public  RestResponse delDutyOrgan(@RequestParam("id") String id){
        try {
            boolean delflag = dutyOrganService.delDutyOrgan(id);
            return RestResponse.success().setData(delflag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyOrganInfo")
    @ApiOperation("获取机构信息")
    public  RestResponse getDutyOrganInfo(@RequestParam("id") String id){
        try {
            DutyOrgan dutyOrgan = dutyOrganService.getDutyOrganById(id);
            return RestResponse.success().setData(dutyOrgan);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

}
