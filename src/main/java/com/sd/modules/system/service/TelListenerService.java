package com.sd.modules.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Chen Hualiang
 * @create 2021-03-08 15:39
 */
public interface TelListenerService {
    //监听到有新增电话录音上传录音
    String uploadTelFile(MultipartFile file , String dir) throws FileNotFoundException, IOException;
}
