package com.sd.modules.system.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Email {
    
    @TableId
    private Long id;

    /**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件内容
	 */
    private String content;
    
    /**
     * 附件 ,"|" 分割
     */
    private String files;

    /**
	 * 发送方的地址
	 */
    private String fromAddress;
    
    /**
	 * 发送的时间
	 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sentDate;
    
    /**
	 * 此邮件的Message-ID
	 */
    @JsonIgnore
    private String msgId;

    /**
     * 状态0未读，1已读，2已回复
     */
    private Integer status;

    /**
     * 回复内容
     */
    private String reply;
    
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
