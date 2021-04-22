package com.sd.modules.system.service;

import com.sd.pojo.DutyAddress;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-28 10:53
 */
public interface DutyAddressService {
    List<DutyAddress> getDutyAddress(int start, int end);

    int getDutyAddressCount();

    boolean checkAddress(String address);

    int saveAddress(DutyAddress saveAddress);

    boolean delAddress(String id);

    DutyAddress getAddressInfo(String id);
}
