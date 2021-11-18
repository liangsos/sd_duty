package com.sd.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sd.modules.system.entity.Email;
import com.sd.modules.system.service.EmailService;
import com.sd.util.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "邮件")
@RequestMapping("/api/email")
@CrossOrigin
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/getEmails")
    @ApiOperation("分页查询邮件")
    public RestResponse getEmails(@RequestParam(value = "current", defaultValue = "1") long current,
            @RequestParam(value = "size", defaultValue = "10") long size,@RequestParam("beginTime") String beginTime,@RequestParam("endTime") String endTime) {
        IPage<Email> page = emailService.getEmails(current, size,beginTime,endTime);

        return RestResponse.success("获取成功").setData(page);
    }

    @PostMapping("/reply")
    @ApiOperation("邮件回复")
    public RestResponse replyEmail(Long id, String content) {
        emailService.replyEmail(id, content);
        return RestResponse.success("回复成功");
    }
}
