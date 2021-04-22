package com.sd.modules.system.service;

import java.util.Date;

/**
 * @author Chen Hualiang
 * @create 2020-10-20 11:08
 */
public interface DutyRecordDetailService {
    boolean addDutyRecordDetail(Date time, String member, String s);

    boolean updateDutyRecordDetail(Date time, String member, String s);

    boolean updateDutyRecordDetail();
}
