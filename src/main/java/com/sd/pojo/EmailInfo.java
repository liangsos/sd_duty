package com.sd.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 读取邮件的POJO
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 10:20:32 PM
 */
@Data
public class EmailInfo {

	/**
	 * 接收方的邮箱地址
	 */
	private String[] toAddress;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件内容
	 */
	private String content;

	/**
	 * 待上传附件的路径及名称、或下载附件的地址及名称
	 */
	private List<String> attachmentFiles = new ArrayList<>();

	/**
	 * 发送方的地址
	 */
	private String fromAddress;

	/**
	 * 发送的时间
	 */
	private Date sentDate;

	/**
	 * 是否需要邮件回执
	 */
	private boolean needReply;

	/**
	 * 是否已读
	 */
	private boolean isReaded;

	/**
	 * 是否包含附件
	 */
	private boolean containsAttachments = false;

	/**
	 * 抄送
	 */
	private String[] carbonCopy;

	/**
	 * 暗抄送
	 */
	private String[] darkCopy;

	/**
	 * 此邮件的Message-ID
	 */
	private String messageID;


	public void setContent(String content) {
		this.content = content == null ? null : content.endsWith("<br>") ? content.replace("<br>", "") : content;
	}



}