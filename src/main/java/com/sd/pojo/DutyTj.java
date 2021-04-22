package com.sd.pojo;

import lombok.Data;

/**
 * @author Chen Hualiang
 * @create 2020-10-21 19:56
 */
@Data
public class DutyTj {
    /**
     * 姓名
     */
    private String name;

    /**
     * 角色
     */
    private String type;
    
    /**
     * 工作日
     */
    private String workday;

    /**
     * 节假日
     */
    private String holiday;
    
    /**
     * 工作日合计
     */
    private String sumWorkday;

    /**
     * 节假日合计
     */
    private String sumHoliday;
    
}
