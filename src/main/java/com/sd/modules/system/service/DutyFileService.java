package com.sd.modules.system.service;

import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sd.pojo.*;

public interface DutyFileService {

    /**
     * 根据id获取会商记录
     * @param id
     * @return
     */
    DutyConsult getDutyConsultById(Integer id);

    /**
     * 获取值班字典表
     * @param type
     * @return
     */
	List<DutyDict> getDutyDict(String type);

    /**
     * 获取会商记录
     * @param beginTime
     * @param endTime
     * @param current 当前页
     * @param pageSize 每页条数
     * @return
     */
    List<DutyConsult> getDutyConsult(String beginTime, String endTime, long current, long size);

    /**
     * 获取共享文件列表
     * @param beginTime
     * @param endTime
     * @param fileType
     * @param current
     * @param size
     * @return
     */
    List<DutyDoc> getDutyDoc(String fileType,String beginTime, String endTime, long current, long size, String isHome);

    /**
     * 保存会商记录
     * @param dutyConsult
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */
	DutyConsult saveDutyConsult(DutyConsult dutyConsult) throws IllegalStateException, IOException;

    /**
     * 保存共享文件
     * @param dutyDoc
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */
	DutyDoc saveDutyDoc(DutyDoc dutyDoc) throws IllegalStateException, IOException;

    /**
     * 会商记录总数
     * @param beginTime
     * @param endTime
     * @return
     */
    int getDutyConsultCount(String beginTime, String endTime);

    /**
     * 删除会商记录
     * @param id
     * @return
     */
    boolean delDutyConsult(String id);

    int getDutyDocCount(String fileType, String beginTime, String endTime,String isHome);

    DutyDoc getDutyDocById(String id);

    boolean delDutyDoc(String id);

    List<DutyMat> getDutyMat(String beginTime, String endTime, int current, int size,String isHome);

    int getDutyMatCount(String beginTime, String endTime, String isHome);

    DutyMat saveDutyMat(DutyMat dutyMat) throws IOException;

    DutyMat getDutyMatById(String id);

    boolean delDutyMat(String id);

    DutyAnno saveDutyAnno(DutyAnno dutyAnno) throws IOException;

    List<DutyAnno> getDutyAnno(String beginTime, String endTime, int current, int size,String isHome);

    int getDutyAnnoCount(String beginTime, String endTime,String isHome);

    DutyAnno getDutyAnnoById(String id);

    boolean delDutyAnno(String id);

    /**
     * 获取阶段材料列表
     * @param beginTime
     * @param endTime
     * @param current
     * @param size
     * @return
     */
//    List<DutyMat> getDutyMat(String beginTime, String endTime, long current, long size);
}
