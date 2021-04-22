package com.sd.modules.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.sd.modules.system.mapper.DutyOrganMapper;
import com.sd.modules.system.mapper.DutyRecordTelMapper;
import com.sd.modules.system.service.TelListenerService;
import com.sd.pojo.DutyRecordTel;
import com.sd.realm.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Chen Hualiang
 * @create 2021-03-08 15:44
 */
@Service
public class TelListenerServiceImpl implements TelListenerService {

    @Value("${duty.file-root-path}")
    private String fileRootPath;

    @Autowired
    private DutyRecordTelMapper dutyRecordTelMapper;

    @Override
    public String uploadTelFile(MultipartFile file, String dir) throws FileNotFoundException, IOException{
        String imgURL = null;

        Date now = new Date();
        DutyRecordTel dutyRecordTel = new DutyRecordTel();
        dutyRecordTel.setUpdateDate(now);
        dutyRecordTel.setUpdateUser("自动存储");
        String fileName = file.getOriginalFilename();
        if (fileName.contains("A")){//来电
            dutyRecordTel.setType("0");
        }
        if (fileName.contains("B")){//去电
            dutyRecordTel.setType("1");
        }
        String[] emels = fileName.split("-");
        String telephone = emels[3];
        System.out.println("电话号码：" + telephone);
        dutyRecordTel.setTelephone(telephone);
        if (file != null){
            String originalFileName = file.getOriginalFilename();
            String path = "telephone-files/";
            String newFileName = DateUtil.format(now, DatePattern.PURE_DATETIME_PATTERN)
                    + originalFileName.substring(originalFileName.lastIndexOf("."));
            File f = new File(fileRootPath + path, newFileName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            file.transferTo(f);
            dutyRecordTel.setAudio(path + newFileName);
        }
        int i = 0;
//        i = dutyRecordTelMapper.insert(dutyRecordTel);
        if (i != 1) {
            throw new RuntimeException("保存来电记录失败！");
        }

        return imgURL;
    }
}
