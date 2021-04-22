package com.sd.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * 发件人白名单
 */
@Data
public class SenderWhiteList {
    
    @TableId
    private Long id;

    private String email;

    private String remark;
}
