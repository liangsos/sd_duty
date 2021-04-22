package com.sd.modules.system.service;

import com.sd.pojo.DutyFileOrgan;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-27 10:05
 */
public interface DutyFileOrganService {
    /**
     * 获取来文单位列表
     * @param start
     * @param end
     * @return
     */
    List<DutyFileOrgan> getDutyFileOrgan(int start, int end);

    int getDutyFileOrganCount();

    int getDutyFileSort();

    boolean checkFileOrgan(String name);

    int saveDutyOrgan(DutyFileOrgan saveOrgan);

    boolean delDutyFileOrgan(String id);

    DutyFileOrgan getDutyFileOrganById(String id);
}
