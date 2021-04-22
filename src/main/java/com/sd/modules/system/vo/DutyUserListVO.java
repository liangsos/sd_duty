package com.sd.modules.system.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Hualiang
 * @create 2020-10-26 10:27
 */
@Data
public class DutyUserListVO {
    private Integer  draw;;
    private long recordsTotal;;
    private long recordsFiltered;;
    private List<Map<String,Object>> data;
}
