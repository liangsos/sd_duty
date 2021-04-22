package com.sd.modules.system.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sd.pojo.DutyFxkh;
import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordTel;

/**
 * @author Chen Hualiang
 * @create 2020-10-15 15:58
 */
public interface DutyBbService {
    /**
     * 获取值班记录
     * @param beginTime
     * @param endTime
     * @param start
     * @param end
     * @return
     */
    List<DutyRecord> getDutyBb(String beginTime, String endTime, int start, int end);

    /**
     * 值班文档总数
     * @param beginTime
     * @param endTime
     * @return
     */
    int getDutyBbCount(String beginTime,String endTime);

    /**
     * 获取当天值班记录
     * @param date
     * @return
     */
    List<DutyRecord> getDutyRecordToday(String date);

    /**
     * 根据时间获取防汛抗旱工作
     * @param date
     * @return
     */
    List<DutyFxkh> getFxkhByTime(String date);

    /**
     * 根据时间获取来电记录
     * @param date
     * @return
     */
    List<DutyRecordTel> getDutyRecordTel(String date) throws IOException;

    /**
     * 根据时间获取会商记录
     * @param date
     * @return
     */
    List<Map<String, Object>> getDutyConsultByTime(String date);
}
