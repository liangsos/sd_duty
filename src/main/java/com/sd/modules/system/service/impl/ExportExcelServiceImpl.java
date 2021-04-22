package com.sd.modules.system.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.sd.modules.system.service.DutyRecordUserService;
import com.sd.modules.system.service.ExportExcelService;
import com.sd.pojo.DutyRecord;
import com.sd.realm.AuthRealm;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Chen Hualiang
 * @create 2020-10-30 10:38
 */
@Service
public class ExportExcelServiceImpl implements ExportExcelService {

    @Value("${excel.path}")
    private String excelPath;
    @Value("${excel.host}")
    private String excelHost;
    @Value("${duty.file-root-path}")
    private String fileRootPath;

    @Autowired
    private DutyRecordUserService dutyRecordUserService;


    @Override
    public String exportDutyExcel(String beginTime, String endTime) throws IOException {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        TemplateExportParams params =new TemplateExportParams(excelPath + "template/DutyExcel.xls");
        //获取值班记录
        List<DutyRecord> recordList = dutyRecordUserService.getDutyRecord(beginTime,endTime,addvcd);
        Map<String,Object> outmap = new HashMap<>();
        outmap.put("beginTime",DateUtil.format(DateUtil.parse(beginTime),"yyyy年M月d日"));
        outmap.put("endTime",DateUtil.format(DateUtil.parse(endTime),"yyyy年M月d日"));
        List<Map<String,Object>> listMap = new ArrayList<>();
        for (DutyRecord record : recordList) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", DateUtil.format(record.getTime(),"M月d日"));
            map.put("leaderComm",record.getLeaderComm().trim());
            map.put("leader",record.getLeader());
            map.put("member",record.getMember());

            listMap.add(map);
        }
        outmap.put("maplist",listMap);
        Workbook workbook = ExcelExportUtil.exportExcel(params,outmap);
        String excelDir = "DutyRecord/";
        File savefile = new File(fileRootPath + excelDir);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        String fileName = "值班表" + DateUtil.format(DateUtil.parse(beginTime), DatePattern.PURE_DATE_PATTERN) + "-"
                + DateUtil.format(DateUtil.parse(endTime), DatePattern.PURE_DATE_PATTERN) + ".xlsx";
        FileOutputStream fos = new FileOutputStream(fileRootPath + excelDir + fileName);
        workbook.write(fos);
        fos.close();
        return excelHost + excelDir + fileName;
    }
}
