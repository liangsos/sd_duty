package com.sd.modules.system.vo;

import lombok.Data;

/**
 * @author Chen Hualiang
 * @create 2020-10-19 10:05
 */
@Data
public class DutyRecordPlanVO {
    private String id;
    private String start;
    private String title;
    private String url;
    private String className;
    private boolean allDay;
    private boolean editable;
    private String leaderComm;
    private String leader;
    private String member;
}
