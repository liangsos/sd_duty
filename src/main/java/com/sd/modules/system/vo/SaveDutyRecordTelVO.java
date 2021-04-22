package com.sd.modules.system.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class SaveDutyRecordTelVO {

    private Integer id;
    private String time;
    private String type;
    private String units;
    private String telephone;
    private String content;
    private MultipartFile file;
}
