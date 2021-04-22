package com.sd.modules.system.controller;

import com.sd.modules.system.service.DutyHolidayService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.DutyHoliday;
import com.sd.realm.AuthRealm;
import com.sd.util.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 法定假管理
 * @author Chen Hualiang
 * @create 2020-10-27 17:35
 */
@RestController
@RequestMapping("/api/dutyHoliday")
@Api(tags = "法定假管理")
public class DutyHolidayController {

    @Autowired
    protected DutyHolidayService dutyHolidayService;

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/getDutyHoliday")
    @ApiOperation("获取法定节假日列表")
    public RestResponse getDutyHoliday(@RequestBody List<TableData> list){
        try {
            int sEcho = 0;
            int iDisplayStart = 0;
            int iDisplayLength = 0;
            //查询年度
            String searchYear = "";

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
                if (tableData.getName().equals("search_year")){
                    searchYear = tableData.getValue();
                }
            }

            List<DutyHoliday> holidays = dutyHolidayService.getDutyHoliday(searchYear,iDisplayStart,iDisplayStart + iDisplayLength);

            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyHoliday holiday : holidays) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",holiday.getId());
                map.put("time",sf.format(holiday.getTime()));
                map.put("type",holiday.getType());
                map.put("comments",holiday.getComments());
                dataList.add(map);
            }

            int total = dutyHolidayService.getDutyHolidayCount(searchYear);
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

    @PostMapping("/checkHoliday")
    @ApiOperation("校验日期是否重复")
    public RestResponse checkHoliday(@RequestParam("date") String date){
        try {
            boolean isExist = dutyHolidayService.checkHoliday(date);
            return RestResponse.success().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveHoliday")
    @ApiOperation("保存法定假")
    public  RestResponse saveHoliday(@RequestBody DutyHoliday dutyHoliday){
        try {
            AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            DutyHoliday saveHoliday = new DutyHoliday();
            saveHoliday.setId(dutyHoliday.getId());
            saveHoliday.setTime(dutyHoliday.getTime());
            saveHoliday.setType(dutyHoliday.getType());
            saveHoliday.setUpdateUser(user.getuserName());
            saveHoliday.setComments(dutyHoliday.getComments());

            int res = dutyHolidayService.saveHoliday(saveHoliday);

            return RestResponse.success().setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delHoliday")
    @ApiOperation("删除法定假")
    public  RestResponse delHoliday(@RequestParam("id") String id){
        try {
            boolean delflag = dutyHolidayService.delHoliday(id);
            return RestResponse.success().setData(delflag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getHolidayInfo")
    @ApiOperation("获取法定假信息")
    public  RestResponse getHolidayInfo(@RequestParam("id") String id){
        try {
            DutyHoliday holiday = dutyHolidayService.getHolidayInfo(id);
            return RestResponse.success().setData(holiday);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

}
