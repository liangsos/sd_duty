package com.sd.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.Console;
import com.sd.modules.system.service.TelListenerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author Chen Hualiang
 * @create 2021-03-08 15:13
 */
@Slf4j
@Component
public class FileListenerRunner implements CommandLineRunner {

    @Autowired
    private TelListenerService telListenerService;

    @Override
    public void run(String... args) throws Exception {
        File file = FileUtil.file("E:\\TxRec");
        //这里只监听文件或目录的创建和删除事件
        WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.ENTRY_CREATE,WatchMonitor.ENTRY_DELETE);
        watchMonitor.setWatcher(new Watcher(){
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                try {
                    String obj = event.context().toString();
                    log.info("创建：{}-> {}", currentPath, obj);
                    File telFile = new File(currentPath + "\\" + obj);
                    FileInputStream input = new FileInputStream(telFile);
                    MultipartFile multipartFile = new MockMultipartFile("copy"+telFile.getName(),telFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(),input);
                    telListenerService.uploadTelFile(multipartFile,currentPath.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                log.info("修改：{}-> {}", currentPath, obj);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                log.info("删除：{}-> {}", currentPath, obj);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                log.info("Overflow：{}-> {}", currentPath, obj);
            }
        });

        //设置监听目录的最大深入，目录层级大于制定层级的变更将不被监听，默认只监听当前层级目录
        watchMonitor.setMaxDepth(4);
        //启动监听
        watchMonitor.start();

    }
}
