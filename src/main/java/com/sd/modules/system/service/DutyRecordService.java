package com.sd.modules.system.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.sd.modules.system.vo.DutyRecordTjVO;
import com.sd.modules.system.vo.SaveDutyRecordTelVO;
import com.sd.pojo.*;
import org.springframework.web.multipart.MultipartFile;

public interface DutyRecordService {

    /**
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    List<DutyRecordTjVO> getDutyRecord(String beginTime, String endTime);

    /**
     * 判断当前用户是否为最新的接班人员
     * 
     * @param time
     * @return
     */
    boolean isDutyUser(String time);

    /**
     * 根据时间获取防汛抗旱工作
     * 
     * @param time
     * @return
     */
    List<DutyFxkh> getFxkhByTime(String time);

    /**
     * 根据类别获取雨水情主要站点
     * 
     * @param type
     * @return
     */
    List<DutyStation> getDutyStationByType(String type);

    /**
     * 值班-获取所有的来文单位
     * 
     * @return
     */
    List<DutyFileOrgan> getDutyFileOrganAll();

    /**
     * 保存防汛抗旱工作
     * 
     * @param dutyFxkh
     */
    DutyFxkh saveDutyFxkh(DutyFxkh dutyFxkh);

    /**
     * 保存来电记录
     * 
     * @param dutyRecordTel
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */
    DutyRecordTel saveDutyRecordTel(SaveDutyRecordTelVO sdrt) throws IllegalStateException, IOException;

    /**
     * 删除来电记录
     * 
     * @param id
     */
    void delDutyRecordTel(Integer id);

    /**
     * 获取当天值班情况
     * @param time
     * @return
     */
    DutyRecord getDutyRecordToday(Date time,String addvcd);

    /**
     * 保存值班记录
     * @param dutyRecord
     * @return
     */
    int saveDutyRecord(DutyRecord dutyRecord);

    /**
     * 值班机构
     * @return
     */
	List<DutyOrgan> getDutyOrgan();

    /**
     * 交换班
     * @param member
     * @param memberOld
     * @return
     */
    int handOver(String member, String memberOld);

    /**
     * 更新值班记录表交接班标识为1
     * @param dutyRecord
     * @return
     */
    int updateDutyFlag(DutyRecord dutyRecord);

    /**
     * 根据Id获取值班记录
     * @param id
     * @return
     */
    DutyRecord getDutyRecordById(String id);

    /**
     * 根据id获取来电记录
     * @param id
     * @return
     */
    DutyRecordTel getDutyRecordTelById(String id);

    /**
     * 获取电话录音文件
     * @param addvcd 行政区划
     * @param date 日期
     * @return
     */
    DutyRecordTel getTelFile(String addvcd, String date, MultipartFile file) throws IOException;
}
