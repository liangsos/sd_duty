package com.sd.modules.system.service;

import com.sd.pojo.DutyHoliday;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 17:51
 */
public interface DutyHolidayService {

    List<DutyHoliday> getDutyHoliday(String searchYear, int start, int end);

    int getDutyHolidayCount(String searchYear);

    boolean checkHoliday(String date);

    int saveHoliday(DutyHoliday saveHoliday);

    boolean delHoliday(String id);

    DutyHoliday getHolidayInfo(String id);
}
