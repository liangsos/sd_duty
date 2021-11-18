package com.sd.modules.system.controller;

import com.sd.modules.system.service.DutyFileOrganService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.DutyFileOrgan;
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
 * @create 2020-10-27 10:04
 */
@RestController
@RequestMapping("/api/dutyFileOrgan")
@CrossOrigin
@Api(tags = "来文单位")
public class DutyFileOrganController {
    @Autowired
    protected DutyFileOrganService dutyFileOrganService;

    @PostMapping("/getDutyFileOrgan")
    @ApiOperation("获取来文单位列表")
    public RestResponse getDutyFileOrgan(@RequestBody List<TableData> list){
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

            List<DutyFileOrgan> fileOrgans = dutyFileOrganService.getDutyFileOrgan(iDisplayStart,iDisplayStart + iDisplayLength);
            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyFileOrgan organ : fileOrgans) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",organ.getId());
                map.put("name",organ.getName());
                map.put("abbr",organ.getAbbr());
                map.put("comments",organ.getComments());
                map.put("sort",organ.getSort());
                dataList.add(map);
            }

            int total = dutyFileOrganService.getDutyFileOrganCount();
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

    @PostMapping("/getDutyFileSort")
    public RestResponse getDutyFileSort(){
        try {
            int count = dutyFileOrganService.getDutyFileSort();
            return RestResponse.success().setData(count);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/checkFileOrgan")
    @ApiOperation("校验来文单位名称")
    public RestResponse checkFileOrgan(@RequestParam("name") String name){
        try {
            boolean isExist = dutyFileOrganService.checkFileOrgan(name);
            return RestResponse.success().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveDutyFileOrgan")
    @ApiOperation("新增或修改来文单位")
    public  RestResponse saveDutyFileOrgan(@RequestBody DutyFileOrgan dutyFileOrgan){
        try {
            DutyFileOrgan saveOrgan = new DutyFileOrgan();
            saveOrgan.setId(dutyFileOrgan.getId());
            saveOrgan.setName(dutyFileOrgan.getName());
            saveOrgan.setAbbr(dutyFileOrgan.getAbbr());
            saveOrgan.setSort(dutyFileOrgan.getSort());
            saveOrgan.setComments(dutyFileOrgan.getComments());

            int res = dutyFileOrganService.saveDutyOrgan(saveOrgan);

            return RestResponse.success().setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delDutyFileOrgan")
    @ApiOperation("删除来文单位")
    public  RestResponse delDutyFileOrgan(@RequestParam("id") String id){
        try {
            boolean delflag = dutyFileOrganService.delDutyFileOrgan(id);
            return RestResponse.success().setData(delflag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyFileOrganInfo")
    @ApiOperation("获取机构信息")
    public  RestResponse getDutyFileOrganInfo(@RequestParam("id") String id){
        try {
            DutyFileOrgan dutyFileOrgan = dutyFileOrganService.getDutyFileOrganById(id);
            return RestResponse.success().setData(dutyFileOrgan);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }
}
