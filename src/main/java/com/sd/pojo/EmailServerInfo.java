package com.sd.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 登陆的邮箱服务器的信息，包括服务器的 host 和 ip，用户名和密码等
 */
@Data
@Component
@ConfigurationProperties(prefix = "email")
public class EmailServerInfo {
    
    /**
	 * 发送邮件的服务器的IP地址
	 */
	private String mailServerPOP3Host;
	private String mailServerSMTPHost;

	/**
	 * 登陆的邮箱
	 */
	private String myEmailAddress;

	/**
	 * 登陆邮件发送服务器的用户名和密码
	 */
	private String userName;
	private String password;

	/**
	 * 是否需要身份验证，默认为true
	 */
	private boolean validate = true;

	/**
	 * 是否支持ssl链接
	 */
	private boolean ssl = true;
}
