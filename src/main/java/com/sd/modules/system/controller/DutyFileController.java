package com.sd.modules.system.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sd.modules.system.service.DutyFileService;
import com.sd.modules.system.vo.DutyUserListVO;
import com.sd.modules.system.vo.TableData;
import com.sd.pojo.*;
import com.sd.util.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/file")
@CrossOrigin
@Api(tags = "文件管理")
public class DutyFileController {

    @Value("${duty.file-root-path}")
    private String fileRootPath;

    @Autowired
    private DutyFileService dutyFileService;

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/getDutyConsultById")
    @ApiOperation("根据id获取会商记录")
    public RestResponse getDutyConsultById(Integer id) {
        try {
            DutyConsult dutyConsult = dutyFileService.getDutyConsultById(id);
            return RestResponse.success().setData(dutyConsult);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/GetDutyDict")
    @ApiOperation("获取值班字典表")
    public RestResponse getDutyDict(String type) {
        try {
            List<DutyDict> list = dutyFileService.getDutyDict(type);
            return RestResponse.success().setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/GetDutyConsult")
    @ApiOperation("获取会商记录")
    public RestResponse getDutyConsult(@RequestBody List<TableData> list) {
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
            List<DutyConsult> consults = dutyFileService.getDutyConsult(search_begin_time,search_end_time,iDisplayStart,iDisplayStart + iDisplayLength);

            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyConsult consult : consults) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",consult.getId());
                map.put("time",sf.format(consult.getBeginTime()));
                map.put("place",consult.getPlace());
                map.put("host",consult.getHost());
                map.put("attendees",consult.getAttendees());
                map.put("attend",consult.getAttend());
                map.put("content",consult.getContent());
                map.put("docment",consult.getDocment());
                map.put("updateUser",consult.getUpdateUser());
                map.put("realName",consult.getRealName());
                dataList.add(map);
            }
            int total = dutyFileService.getDutyConsultCount(search_begin_time,search_end_time);
            DutyUserListVO model = new DutyUserListVO();
            model.setDraw(sEcho);
            model.setRecordsTotal(total);
            model.setRecordsFiltered(total);
            model.setData(dataList);
//            IPage<DutyConsult> page = dutyFileService.getDutyConsult(beginTime, endTime, current, size);
            return RestResponse.success().setData(model);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/GetDutyDoc")
    @ApiOperation("获取共享文档")
    public RestResponse getDutyDoc(@RequestBody List<TableData> list) {
        try {
            int sEcho = 0;
            int iDisplayStart = 0;
            int iDisplayLength = 0;

            //查询时间
            String search_type = "";
            String search_begin_time = "";
            String search_end_time = "";

            //是否为首页查询
            String isHome = "";

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
                if (tableData.getName().equals("search_type")){
                    search_type = tableData.getValue();
                }
                if (tableData.getName().equals("isHome")){
                    isHome = tableData.getValue();
                }
            }

            List<DutyDoc> dutyDocs = dutyFileService.getDutyDoc(search_type,search_begin_time,search_end_time,iDisplayStart,iDisplayStart + iDisplayLength,isHome);

            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyDoc dutyDoc : dutyDocs) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",dutyDoc.getId());
                map.put("fileType",dutyDoc.getFileType());
                map.put("fileName",dutyDoc.getFileName());
                map.put("soure",dutyDoc.getSource());
                map.put("content",dutyDoc.getContent());
                map.put("updateDate",sf1.format(dutyDoc.getUpdateDate()));
                dataList.add(map);
            }

            int total = dutyFileService.getDutyDocCount(search_type,search_begin_time,search_end_time,isHome);

            DutyUserListVO model = new DutyUserListVO();
            model.setDraw(sEcho);
            model.setRecordsTotal(total);
            model.setRecordsFiltered(total);
            model.setData(dataList);

//            IPage<DutyDoc> page = dutyFileService.getDutyDoc(beginTime, endTime, fileType, current, size);
            return RestResponse.success().setData(model);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/GetDutyMat")
    @ApiOperation("获取阶段材料")
    public RestResponse getDutyMat(@RequestBody List<TableData> list) {
        try {
            int sEcho = 0;
            int iDisplayStart = 0;
            int iDisplayLength = 0;

            //查询时间
            String search_begin_time = "";
            String search_end_time = "";

            //是否为首页查询
            String isHome = "";

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
                if (tableData.getName().equals("isHome")){
                    isHome = tableData.getValue();
                }
            }

            List<DutyMat> dutyMats = dutyFileService.getDutyMat(search_begin_time,search_end_time,iDisplayStart,iDisplayStart + iDisplayLength,isHome);

            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyMat dutyMat : dutyMats) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",dutyMat.getId());
                map.put("fileName",dutyMat.getFileName());
                map.put("soure",dutyMat.getSource());
                map.put("content",dutyMat.getContent());
                map.put("updateDate",sf1.format(dutyMat.getUpdateDate()));
                dataList.add(map);
            }

            int total = dutyFileService.getDutyMatCount(search_begin_time,search_end_time,isHome);

            DutyUserListVO model = new DutyUserListVO();
            model.setDraw(sEcho);
            model.setRecordsTotal(total);
            model.setRecordsFiltered(total);
            model.setData(dataList);

//            IPage<DutyDoc> page = dutyFileService.getDutyDoc(beginTime, endTime, fileType, current, size);
            return RestResponse.success().setData(model);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/GetDutyAnno")
    @ApiOperation("获取系统公告")
    public RestResponse getDutyAnno(@RequestBody List<TableData> list) {
        try {
            int sEcho = 0;
            int iDisplayStart = 0;
            int iDisplayLength = 0;

            //查询时间
            String search_begin_time = "";
            String search_end_time = "";

            //是否为首页查询
            String isHome ="";

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
                if (tableData.getName().equals("isHome")){
                    isHome = "1";
                }
            }

            List<DutyAnno> dutyAnnos = dutyFileService.getDutyAnno(search_begin_time,search_end_time,iDisplayStart,iDisplayStart + iDisplayLength,isHome);

            List<Map<String,Object>> dataList = new ArrayList<>();
            for (DutyAnno dutyAnno : dutyAnnos) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",dutyAnno.getId());
                map.put("fileName",dutyAnno.getFileName());
                map.put("soure",dutyAnno.getSource());
                map.put("content",dutyAnno.getContent());
                map.put("updateDate",sf1.format(dutyAnno.getUpdateDate()));
                dataList.add(map);
            }

            int total = dutyFileService.getDutyAnnoCount(search_begin_time,search_end_time,isHome);

            DutyUserListVO model = new DutyUserListVO();
            model.setDraw(sEcho);
            model.setRecordsTotal(total);
            model.setRecordsFiltered(total);
            model.setData(dataList);

//            IPage<DutyDoc> page = dutyFileService.getDutyDoc(beginTime, endTime, fileType, current, size);
            return RestResponse.success().setData(model);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveDutyConsult")
    @ApiOperation("保存会商记录")
    public RestResponse saveDutyConsult(DutyConsult dutyConsult) {
        try {
            dutyConsult = dutyFileService.saveDutyConsult(dutyConsult);
            return RestResponse.success().setData(dutyConsult.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/saveDutyDoc")
    @ApiOperation("保存共享文件")
    public RestResponse saveDutyDoc(DutyDoc dutyDoc) {
        try {
            dutyDoc = dutyFileService.saveDutyDoc(dutyDoc);
            return RestResponse.success().setData(dutyDoc.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }

    }

    @PostMapping("/saveDutyMat")
    @ApiOperation("保存阶段材料")
    public RestResponse saveDutyMat(DutyMat dutyMat) {
        try {
            dutyMat = dutyFileService.saveDutyMat(dutyMat);
            return RestResponse.success().setData(dutyMat.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }

    }

    @PostMapping("/saveDutyAnno")
    @ApiOperation("保存系统公告")
    public RestResponse saveDutyAnno(DutyAnno dutyAnno) {
        try {
            dutyAnno = dutyFileService.saveDutyAnno(dutyAnno);
            return RestResponse.success().setData(dutyAnno.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }

    }

    @PostMapping("/delDutyConsult")
    @ApiOperation("删除会商记录")
    public  RestResponse delDutyConsult(@RequestParam("id") String id){
        try {
            boolean delflag = dutyFileService.delDutyConsult(id);
            return RestResponse.success().setData(delflag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyDocById")
    @ApiOperation("根据ID获取共享文件")
    public RestResponse getDutyDocById(@RequestParam("id") String id){
        try {
            DutyDoc dutyDoc = dutyFileService.getDutyDocById(id);
            return RestResponse.success().setData(dutyDoc);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyMatById")
    @ApiOperation("根据ID获取阶段材料")
    public RestResponse getDutyMatById(@RequestParam("id") String id){
        try {
            DutyMat dutyMat = dutyFileService.getDutyMatById(id);
            return RestResponse.success().setData(dutyMat);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/getDutyAnnoById")
    @ApiOperation("根据ID获取系统公告")
    public RestResponse getDutyAnnoById(@RequestParam("id") String id){
        try {
            DutyAnno dutyAnno = dutyFileService.getDutyAnnoById(id);
            return RestResponse.success().setData(dutyAnno);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delDutyDoc")
    @ApiOperation("删除共享文件")
    public RestResponse delDutyDoc(@RequestParam("id") String id){
        try {
            boolean flag = dutyFileService.delDutyDoc(id);
            return RestResponse.success().setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delDutyMat")
    @ApiOperation("删除阶段材料")
    public RestResponse delDutyMat(@RequestParam("id") String id){
        try {
            boolean flag = dutyFileService.delDutyMat(id);
            return RestResponse.success().setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }

    @PostMapping("/delDutyAnno")
    @ApiOperation("删除系统公告")
    public RestResponse delDutyAnno(@RequestParam("id") String id){
        try {
            boolean flag = dutyFileService.delDutyAnno(id);
            return RestResponse.success().setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
    }


//    @PostMapping("/GetDutyMat")
//    @ApiOperation("获取阶段材料")
//    public RestResponse getDutyMat(@RequestBody List<TableData> list) {
//        try {
//            int sEcho = 0;
//            int iDisplayStart = 0;
//            int iDisplayLength = 0;
//
//            //查询时间
//            String search_type = "";
//            String search_begin_time = "";
//            String search_end_time = "";
//
//            for (TableData tableData : list) {
//                if (tableData.getName().equals("sEcho")){
//                    sEcho = Integer.parseInt(tableData.getValue());
//                }
//                if (tableData.getName().equals("iDisplayStart")){
//                    iDisplayStart = Integer.parseInt(tableData.getValue());
//                }
//                if (tableData.getName().equals("iDisplayLength")){
//                    iDisplayLength = Integer.parseInt(tableData.getValue());
//                }
//                if (tableData.getName().equals("search_begin_time")){
//                    search_begin_time = tableData.getValue();
//                }
//                if (tableData.getName().equals("search_end_time")){
//                    search_end_time = tableData.getValue();
//                }
//            }
//
//            List<DutyMat> dutyMats = dutyFileService.getDutyMat(search_begin_time,search_end_time,iDisplayStart,iDisplayStart + iDisplayLength);
//
//            List<Map<String,Object>> dataList = new ArrayList<>();
//            for (DutyMat dutyMat : dutyMats) {
//                Map<String,Object> map = new HashMap<>();
//                map.put("id",dutyMat.getId());
//                map.put("fileName",dutyMat.getFileName());
//                map.put("soure",dutyMat.getSource());
//                map.put("content",dutyMat.getContent());
//                dataList.add(map);
//            }
//
//            int total = dutyFileService.getDutyDocCount(search_type,search_begin_time,search_end_time);
//
//            DutyUserListVO model = new DutyUserListVO();
//            model.setDraw(sEcho);
//            model.setRecordsTotal(total);
//            model.setRecordsFiltered(total);
//            model.setData(dataList);
//
//            return RestResponse.success().setData(model);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return RestResponse.failure(e.getMessage());
//        }
//    }

}
