package com.sd.modules.system.service;

import com.sd.pojo.DutyRecordPlan;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-22 16:50
 */
public interface IImportExcelService {

    /**
     * 获取导入的Excel表中数据
     * @param file
     * @param req
     * @param resp
     * @return
     */
    public List<DutyRecordPlan> importExcel(MultipartFile file, HttpServletRequest req, HttpServletResponse resp);
}
