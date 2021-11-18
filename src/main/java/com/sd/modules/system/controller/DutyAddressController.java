package com.sd.modules.system.controller;

import com.sd.modules.system.service.DutyAddressService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.DutyAddress;
import com.sd.pojo.DutyHoliday;
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

/**
 * 值班IP管理
 * @author Chen Hualiang
 * @create 2020-10-28 10:52
 */
@RestController
@RequestMapping("/api/dutyAddress")
@CrossOrigin
@Api(tags = "值班IP管理")
public class DutyAddressController {

    @Autowired
    protected DutyAddressService dutyAddressService;

    @PostMapping("/getDutyAddress")
    @ApiOperation("获取值班IP列表")
    public RestResponse getDutyAddress(@RequestBody List<TableData> list){
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

            List<DutyAddress> addressList = dutyAddressService.getDutyAddress(iDisplayStart,iDisplayStart + iDisplayLength);

            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyAddress address : addressList) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",address.getId());
                map.put("address",address.getAddress());
                map.put("comments",address.getComments());
                dataList.add(map);
            }

            int total = dutyAddressService.getDutyAddressCount();
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

    @PostMapping("/checkAddress")
    @ApiOperation("校验IP是否重复")
    public RestResponse checkAddress(@RequestParam("address") String address){
        try {
            boolean isExist = dutyAddressService.checkAddress(address);
            return RestResponse.success().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveAddress")
    @ApiOperation("保存值班IP")
    public  RestResponse saveAddress(@RequestBody DutyAddress dutyAddress){
        try {
            AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            String addvcd = user.getAddvcd();
            DutyAddress saveAddress = new DutyAddress();
            saveAddress.setId(dutyAddress.getId());
            saveAddress.setAddress(dutyAddress.getAddress());
            saveAddress.setComments(dutyAddress.getComments());
            saveAddress.setUpdateUser(user.getuserName());
            saveAddress.setAddvcd(addvcd);

            int res = dutyAddressService.saveAddress(saveAddress);

            return RestResponse.success().setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delAddress")
    @ApiOperation("删除IP")
    public  RestResponse delAddress(@RequestParam("id") String id){
        try {
            boolean delflag = dutyAddressService.delAddress(id);
            return RestResponse.success().setData(delflag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getAddressInfo")
    @ApiOperation("获取IP信息")
    public  RestResponse getAddressInfo(@RequestParam("id") String id){
        try {
            DutyAddress address = dutyAddressService.getAddressInfo(id);
            return RestResponse.success().setData(address);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }


}
