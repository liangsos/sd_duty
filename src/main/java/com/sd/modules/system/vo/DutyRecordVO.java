package com.sd.modules.system.vo;

import lombok.Data;

/**
 * @author Chen Hualiang
 * @create 2020-10-19 15:03
 */
@Data
public class DutyRecordVO {
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
    private String comments;
    private String duty;
}
