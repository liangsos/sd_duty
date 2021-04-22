package com.sd.modules.system.service;

import java.io.IOException;

/**
 * @author Chen Hualiang
 * @create 2020-10-30 10:37
 */
public interface ExportExcelService {
    String exportDutyExcel(String beginTime, String endTime) throws IOException;
}
