package com.sd.modules.system.service;

import com.sd.pojo.AddvcdStcd;
import com.sd.pojo.Communication;

import java.util.List;

public interface CommunicationNewService {
    /**
     * 获取地市及县中心信息
     * @return
     */
    List<AddvcdStcd> getAddvcdStcd();

    /**
     * 获取通讯录列表
     * @return
     */
    List<Communication> getCommunicationNew(String addvcd);

}
