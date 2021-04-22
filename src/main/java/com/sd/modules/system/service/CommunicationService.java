package com.sd.modules.system.service;

import com.sd.pojo.Communication;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-11-12 11:22
 */
public interface CommunicationService {
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
     * 保存或修改通讯录人员
     * @param saveComm
     * @return
     */
    int saveCommunication(Communication saveComm);

    /**
     * 删除通讯录人员
     * @param id
     * @return
     */
    boolean delCommunication(String id);

    /**
     * 获取通讯录人员信息
     * @param id
     * @return
     */
    Communication getCommunicationById(String id);
}
