package com.sd.modules.system.service;

import com.sd.pojo.DutyOrgan;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 8:49
 */
public interface DutyOrganService {
    /**
     * 值班-机构列表
     * @param start
     * @param end
     * @return
     */
    List<DutyOrgan> getDutyOrgan(int start, int end);

    /**
     * 机构总数
     * @return
     */
    int getDutyOrganCount();

    /**
     * 校验机构名称
     * @param name
     * @return
     */
    boolean checkOrgan(String name);

    /**
     * 新增或修改机构
     * @param saveOrgan
     * @return
     */
    int saveDutyOrgan(DutyOrgan saveOrgan);

    /**
     * 删除机构
     * @param id
     * @return
     */
    boolean delDutyOrgan(String id);

    /**
     * 获取值班机构信息
     * @param id
     * @return
     */
    DutyOrgan getDutyOrganById(String id);
}
