package com.sd.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyConsultMapper;
import com.sd.modules.system.mapper.DutyFxkhMapper;
import com.sd.modules.system.mapper.DutyRecordMapper;
import com.sd.modules.system.mapper.DutyRecordTelMapper;
import com.sd.modules.system.service.DutyBbService;
import com.sd.pojo.DutyFxkh;
import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordTel;
import com.sd.realm.AuthRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Chen Hualiang
 * @create 2020-10-15 16:15
 */
@Service
@Slf4j
public class DutyBbServiceImpl implements DutyBbService {

    @Value("${duty.file-host}")
    private String fileHost;

    @Autowired
    private DutyRecordMapper dutyRecordMapper;
    @Autowired
    private DutyFxkhMapper dutyFxkhMapper;
    @Autowired
    private DutyRecordTelMapper dutyRecordTelMapper;
    @Autowired
    private DutyConsultMapper dutyConsultMapper;

    private static String path = "E:\\TxRec";

    @Override
    public List<DutyRecord> getDutyBb(String beginTime, String endTime, int start, int end) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyRecord> list = dutyRecordMapper.getDutyBb(beginTime,endTime,start,end,addvcd);

        return list;
    }

    @Override
    public int getDutyBbCount(String beginTime, String endTime) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
        qw.eq("addvcd",addvcd).between("time",beginTime,endTime);
        int count = dutyRecordMapper.selectCount(qw);
        return count;
    }

    @Override
    public List<DutyRecord> getDutyRecordToday(String date) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
        qw.eq("time",date).eq("addvcd",addvcd);
        List<DutyRecord> list = dutyRecordMapper.selectList(qw);
        return list;
    }

    @Override
    public List<DutyFxkh> getFxkhByTime(String date) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        QueryWrapper<DutyFxkh> qw = new QueryWrapper<>();
        qw.eq("time",date).eq("addvcd",addvcd);
        List<DutyFxkh> list = dutyFxkhMapper.selectList(qw);
        return list;
    }

    @Override
//    public List<DutyRecordTel> getDutyRecordTel(String date) {
//        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
//        String addvcd = user.getAddvcd();
//        List<DutyRecordTel> list = dutyRecordTelMapper.getDutyRecordTel(date,addvcd);
//        for (DutyRecordTel dutyRecordTel : list) {
//            dutyRecordTel.setAudio(fileHost + dutyRecordTel.getAudio());
//        }
//        return list;
//    }
    public List<DutyRecordTel> getDutyRecordTel(String date) throws IOException {
//        getFile();
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyRecordTel> list = dutyRecordTelMapper.getDutyRecordTel(date,addvcd);
        for (DutyRecordTel dutyRecordTel : list) {
            dutyRecordTel.setAudio(fileHost + dutyRecordTel.getAudio());
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getDutyConsultByTime(String date) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<Map<String,Object>> list = dutyConsultMapper.getDutyConsultByTime(date,addvcd);
        return list;
    }



//    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 10 * 1000)
    public static void getFile() throws FileNotFoundException, IOException {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                WatchKey key;
                try {
                    WatchService watchService = FileSystems.getDefault().newWatchService();
                    Paths.get(path).register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                    while (true) {
                        File file = new File(path);//path??????????????????
                        File[] files = file.listFiles();
                        System.out.println("??????????????????????????????");
                        key = watchService.take();//???????????????????????????????????????
                        for (WatchEvent<?> event : key.pollEvents()) {
                            String fileName = path+"\\"+event.context();
                            System.out.println("??????????????????????????????"+fileName);
                            File file1 = files[files.length-1];//??????????????????
                            System.out.println(file1.getName());//??????????????????
//                            url = uploadFile(file1,uploadAddres);//???????????????
                        }if (!key.reset()) {
                            break; //????????????
                        }
                    }
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 10000 , 10 * 1000);//??????????????????????????????ms?????????????????????,??????????????????????????????ms???????????????run
    }

    /**
     * ???file?????????????????????dir????????????
     * @param file??????????????????
     * @param dir???????????????
     * @throws FileNotFoundException
     * @throws IOException
     */
    final static String uploadAddres = System.getProperty("catalina.home")+"\\webapps\\TiaoZhanB\\uploadFile";
    public static String uploadFile(File file , String dir) throws FileNotFoundException, IOException {
        String imgURL = null;
        try {
            InputStream in = new FileInputStream(file);
            System.out.println("??????????????????" + dir);
            // ??????????????????
            String fileName = file.getName();
            // ????????????????????????file?????????
            File uploadFile = new File(dir, fileName);
            // ?????????
            OutputStream out = new FileOutputStream(uploadFile);
            // ??????????????????1MB
            byte[] buffer = new byte[1024 * 1024];
            int length;
            // ???????????????????????????????????????
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            // ??????????????????????????????
            imgURL = "uploadFile/" + fileName;

            System.out.println("???????????????"+imgURL);
            // ???????????????????????????????????????
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgURL;
    }
}
