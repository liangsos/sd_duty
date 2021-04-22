package com.sd.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sd.modules.system.entity.Email;

public interface EmailService {

    /**
     * 分页获取邮件
     * @param current
     * @param size
     * @return
     */
	IPage<Email> getEmails(long current, long size,String beginTime,String endTime);

    /**
     * 邮件回复
     * @param id        邮件id
     * @param content   回复内容
     */
	void replyEmail(Long id, String content);
    
}
