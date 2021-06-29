package com.sd.modules.system.service;

import com.sd.pojo.Communication;
import com.sd.pojo.DutyRecord;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2021-06-29 12:39
 */
public interface PhoneService{
    /**
     * 获取通讯录列表
     * @return
     */
    List<Communication> getCommunication();

    /**
     * 获取通讯录总数
     * @return
     */
    int getCommunicationCount();

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
     * 值班记录总数
     * @param beginTime
     * @param endTime
     * @return
     */
    int getDutyBbCount(String beginTime,String endTime);
}
