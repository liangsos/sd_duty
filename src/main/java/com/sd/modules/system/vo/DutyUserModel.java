package com.sd.modules.system.vo;

import com.sd.pojo.DutyRecord;
import lombok.Data;

import java.util.List;

/**
 * @author Chen Hualiang
 * @create 2020-10-15 16:39
 */
@Data
public class DutyUserModel {
    private Integer  draw;;
    private long recordsTotal;;
    private long recordsFiltered;;
    private List<DutyRecord> data;
}
