package com.sd.modules.system.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.sd.modules.system.service.DutyBbService;
import com.sd.modules.system.vo.DutyUserModel;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.DutyFxkh;
import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordTel;
import com.sd.util.RestResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 值班查询
 * @author Chen Hualiang
 * @create 2020-10-15 9:45
 */
@RestController
@RequestMapping("/api/dutyQuery")
@CrossOrigin
@Api(tags = "值班查询")
public class DutyQueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private SimpleDateFormat sf = new SimpleDateFormat("dd日 HH:mm");
    private SimpleDateFormat sf1 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");

    @Autowired
    protected DutyBbService dutyBbService;

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

            List<DutyRecord> dutyRecordList = dutyBbService.getDutyBb(search_begin_time,search_end_time,iDisplayStart,iDisplayStart + iDisplayLength);

            for (DutyRecord dutyRecord : dutyRecordList) {
                if (StringUtils.isBlank(dutyRecord.getDuty())){
                    dutyRecord.setDuty("否");
                }else {
                    dutyRecord.setDuty(dutyRecord.getDuty().equals("1") ? "是" : "否");
                }
            }

            int count = dutyBbService.getDutyBbCount(search_begin_time,search_end_time);

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

    @PostMapping("/getDutyBbInfo")
    @ApiOperation("值班记录详情查询")
    public RestResponse getDutyBbInfo(@RequestParam("date") String date) throws IOException {
        //获取当天值班记录
        List<DutyRecord> listDutyToday = dutyBbService.getDutyRecordToday(date);
        //获取防汛抗旱工作
        List<DutyFxkh> listDutyFxkh = dutyBbService.getFxkhByTime(date);
        //获取来电记录
        List<DutyRecordTel> listTel = dutyBbService.getDutyRecordTel(date);
        //会商记录
//        List<Map<String,Object>> listConsult = dutyBbService.getDutyConsultByTime(date);

        Map<String,Object> dutyMap = Maps.newHashMap();
        if (listDutyToday.size() > 0){
            dutyMap.put("leader",listDutyToday.get(0).getLeader());
            dutyMap.put("member",listDutyToday.get(0).getMember());
        }

        //拼接html
        String str_html = "<span class='title_span'>一、值班概况</span></br>";
        if (listDutyFxkh.size() > 0){
//            if (StringUtils.isNotBlank(listDutyFxkh.get(0).getWaterInfo())){
//                str_html += "<span>水情信息：</br>" + listDutyFxkh.get(0).getWaterInfo() + "</span></br>";
//            }
//            if (StringUtils.isNotBlank(listDutyFxkh.get(0).getWaterMain())){
//                str_html += "<span>主要站点水情：</br>" + listDutyFxkh.get(0).getWaterMain() + "</span></br>";
//             }
            if (StringUtils.isNotBlank(listDutyFxkh.get(0).getRainMain())){
                str_html += "<span>" + listDutyFxkh.get(0).getRainMain() + "</span></br>";
            }
//            if (StringUtils.isNotBlank(listDutyFxkh.get(0).getRainMain())){
//                str_html += "<span>最大雨量点：</br>" + listDutyFxkh.get(0).getRainMain() + "</span></br>";
//            }
        }else {
            str_html += "<span>       无。</span></br>";
        }
        str_html += "<span class='title_span'>二、雨情</span></br>";
        if (StringUtils.isNotBlank(listDutyFxkh.get(0).getRainInfo())){
            str_html += "<span>" + listDutyFxkh.get(0).getRainInfo() + "</span></br>";
        }else{
            str_html += "<span>       无。</span></br>";
        }
        str_html += "<span class='title_span'>二、水情</span></br>";
        if (StringUtils.isNotBlank(listDutyFxkh.get(0).getWaterMain())){
            str_html += "<span>" + listDutyFxkh.get(0).getWaterMain() + "</span></br>";
        }else{
            str_html += "<span>       无。</span></br>";
        }
        //电话记录
        str_html += "<span class='title_span'>三、电话记录</span></br>";
        if (listTel.size() > 0){
            for (DutyRecordTel map : listTel) {
                str_html += "<div class='bb_info_title_time'><span>" +  sf.format(map.getUpdateDate()) +" </span></div>";
                str_html += "<div class='bb_info_detail'><span>" + ("0".equals(map.getType()) ? "来电" : "去电") + map.getUnits().toString().trim()
                        + ",内容：" + map.getContent().toString().trim() + " (记录人:" + map.getRealName().toString() +  ")</span></br></div>";
            }
        }else {
            str_html += "<span>" + "       无。" + "</span></br>";
        }
        //会商记录
//        str_html += "<span class='title_span'>四、会商记录</span></br>";
//        if (listConsult.size() > 0 || listDutyFxkh.size() > 0)
//        {
//            //值班记录中的会商信息
//            if (listDutyFxkh.size() > 0)
//            {
//                str_html += "<span>" + listDutyFxkh.get(0).getConsultTitle() + ":" + listDutyFxkh.get(0).getConsult() + "</span></br>"; ;
//            }
//
//            //文件管理中的会商信息
//            if (listConsult.size() > 0)
//            {
//                for (Map<String, Object> map : listConsult) {
//                    str_html += "<span>" + sf1.format(map.get("beginTime")) + "在" + map.get("place") +"，由" + map.get("host").toString().trim()
//                            + "主持。参加人员:" + map.get("attendees").toString().trim() + ",参加人数:" + map.get("attend").toString().trim()
//                            + ",会商内容:" + map.get("content").toString().trim() + " (记录人:" + map.get("realName")+ ")</span></br>";
//                }
//            }
//        } else {
//            str_html += "<span>" + "       无。" + "</span></br>";
//        }

        //工情灾情
//        str_html += "<span class='title_span'>五、工情灾情</span></br>";
//        if (listDutyFxkh.size() > 0 && StringUtils.isNotBlank(listDutyFxkh.get(0).getDisaster())) {
//            str_html += "<span>" + listDutyFxkh.get(0).getDisaster() + "</span></br>"; ;
//        } else {
//            str_html += "<span>" + "       无。" + "</span></br>";
//        }

        //防汛抗旱行动
//        str_html += "<span class='title_span'>六、防汛抗旱行动</span></br>";
//        if (listDutyFxkh.size() > 0  && StringUtils.isNotBlank(listDutyFxkh.get(0).getAction())) {
//            str_html += "<span>" + listDutyFxkh.get(0).getAction() + "</span></br>"; ;
//        } else {
//            str_html += "<span>" + "       无。" + "</span></br>";
//        }

        dutyMap.put("infoBody",str_html);

        return RestResponse.success().setData(dutyMap);

    }
}
