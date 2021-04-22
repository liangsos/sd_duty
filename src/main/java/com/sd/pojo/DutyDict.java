package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Duty_Dict")
public class DutyDict {
    
    private String type;

    private String dabh;

    private String daxx;

    private String comments;
}
