package com.sd.modules.system.service;

import com.sd.pojo.DutyRole;

/**
 * @author Chen Hualiang
 * @create 2020-12-18 17:37
 */
public interface DutyRoleService {

    DutyRole findRoleByUserId(Integer userId);

    int getDutyUserSort(String userType);

    long saveDutyRole(DutyRole dutyRole);

    long updateDutyRole(DutyRole dutyRole);
}
