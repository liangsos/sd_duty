package com.sd.modules.system.vo;

import lombok.Data;

@Data
public class DutyRecordTjVO {
    
    private String id;
    private String start;
    private String title;
    private String url;
    private String className;
    private boolean allDay;
    private boolean editable;

    private int tel;

    // private int fax;

    private String duty;
}
