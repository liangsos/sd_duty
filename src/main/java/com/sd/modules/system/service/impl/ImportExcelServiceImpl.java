package com.sd.modules.system.service.impl;

import com.sd.base.ImportExcelBaseService;
import com.sd.modules.system.service.IImportExcelService;
import com.sd.pojo.DutyRecordPlan;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-22 17:06
 */
@Service
public class ImportExcelServiceImpl extends ImportExcelBaseService implements IImportExcelService {

    @Override
    public List<DutyRecordPlan> importExcel(MultipartFile file, HttpServletRequest req, HttpServletResponse resp) {
        int rowNum = 0;//已取值的行数
        int colNum = 0;//列号
        int realRowCount = 0;//真正有数据的行数

        //得到工作空间
        Workbook workbook = null;
        try {
            workbook = super.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //得到工作表
        Sheet sheet = super.getSheetByWorkbook(workbook, 0);
        if (sheet.getRow(2000) != null){
            throw new RuntimeException("系统已限制单批次导入必须小于或等于2000笔！");
        }

        realRowCount = sheet.getPhysicalNumberOfRows();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<DutyRecordPlan> list = new ArrayList<>();
        DutyRecordPlan dutyRecordPlan = null;
        for (Row row : sheet) {
            if(realRowCount == rowNum) {
                break;
            }

            if(super.isBlankRow(row)) {//空行跳过
                continue;
            }

            if(row.getRowNum() == -1) {
                continue;
            } else {
                if(row.getRowNum() == 0) {//第一行表头跳过
                    continue;
                }
            }

            rowNum ++;
            colNum = 0;
            dutyRecordPlan = new DutyRecordPlan();
            super.validCellValue(sheet,row,++ colNum,"日期");
            try {
                dutyRecordPlan.setTime(sdf.parse(super.getCellValue(sheet,row,colNum -1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            super.validCellValue(sheet, row, ++ colNum, "局领导");
            dutyRecordPlan.setLeaderComm(super.getCellValue(sheet,row,colNum - 1));

            super.validCellValue(sheet, row, ++ colNum, "带班");
            dutyRecordPlan.setLeader(super.getCellValue(sheet,row,colNum -1));


            super.validCellValue(sheet,row,++ colNum, "值班人员");
            dutyRecordPlan.setMember(super.getCellValue(sheet,row,colNum - 1));



            list.add(dutyRecordPlan);
        }

        return list;
    }
}
