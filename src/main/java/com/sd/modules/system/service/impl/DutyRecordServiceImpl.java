package com.sd.modules.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sd.modules.system.mapper.DutyFileOrganMapper;
import com.sd.modules.system.mapper.DutyFxkhMapper;
import com.sd.modules.system.mapper.DutyOrganMapper;
import com.sd.modules.system.mapper.DutyRecordMapper;
import com.sd.modules.system.mapper.DutyRecordTelMapper;
import com.sd.modules.system.mapper.DutyStationMapper;
import com.sd.modules.system.service.DutyRecordService;
import com.sd.modules.system.vo.DutyRecordTjVO;
import com.sd.modules.system.vo.SaveDutyRecordTelVO;
import com.sd.pojo.DutyFileOrgan;
import com.sd.pojo.DutyFxkh;
import com.sd.pojo.DutyOrgan;
import com.sd.pojo.DutyRecord;
import com.sd.pojo.DutyRecordTel;
import com.sd.pojo.DutyStation;
import com.sd.realm.AuthRealm.ShiroUser;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

@Service
public class DutyRecordServiceImpl implements DutyRecordService {

    @Value("${duty.file-root-path}")
    private String fileRootPath;

    @Autowired
    private DutyRecordMapper dutyRecordMapper;
    @Autowired
    private DutyFxkhMapper dutyFxkhMapper;
    @Autowired
    private DutyStationMapper dutyStationMapper;
    @Autowired
    private DutyFileOrganMapper dutyFileOrganMapper;
    @Autowired
    private DutyRecordTelMapper dutyRecordTelMapper;
    @Autowired
    private DutyOrganMapper dutyOrganMapper;

    @Override
    public List<DutyRecordTjVO> getDutyRecord(String beginTime, String endTime) {
        Date begin = DateUtil.parse(beginTime);
        Date end = DateUtil.parse(endTime);
        if (begin == null || end == null) {
            throw new RuntimeException("时间不能为空！");
        }
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyRecordTjVO> list = dutyRecordMapper.getDutyRecord(begin, end,addvcd);

        for (DutyRecordTjVO dutyRecordTjVO : list) {
            dutyRecordTjVO.setTitle("");
            dutyRecordTjVO.setUrl("");
            dutyRecordTjVO.setClassName("fc-cell-new");
            dutyRecordTjVO.setAllDay(true);
            dutyRecordTjVO.setEditable(false);
        }
        return list;
    }

    @Override
    public boolean isDutyUser(String time) {
        boolean res = false;
        if (DateUtil.isSameDay(DateUtil.parse(time), new Date())) {
            ShiroUser currentUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            QueryWrapper<DutyRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("TOP 1 * ").eq("duty", "1").eq("addvcd",currentUser.getAddvcd()).orderByDesc("time");
            DutyRecord dutyRecord = dutyRecordMapper.selectOne(queryWrapper);
            if (dutyRecord.getMember().contains(currentUser.getrealName())) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public List<DutyFxkh> getFxkhByTime(String time) {
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        Date date = DateUtil.parse(time);
        QueryWrapper<DutyFxkh> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("time", date).eq("addvcd",addvcd);
        List<DutyFxkh> list = dutyFxkhMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<DutyStation> getDutyStationByType(String type) {
        QueryWrapper<DutyStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        List<DutyStation> list = dutyStationMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<DutyFileOrgan> getDutyFileOrganAll() {
        QueryWrapper<DutyFileOrgan> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<DutyFileOrgan> list = dutyFileOrganMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public DutyFxkh saveDutyFxkh(DutyFxkh dutyFxkh) {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        dutyFxkh.setUpdateUser(user.getuserName());
        dutyFxkh.setUpdateDate(new Date());
        dutyFxkh.setAddvcd(addvcd);
        int i = 0;
        if (dutyFxkh.getId() == null) {
            i = dutyFxkhMapper.insert(dutyFxkh);
        } else {
            i = dutyFxkhMapper.updateById(dutyFxkh);
        }
        if (i != 1) {
            throw new RuntimeException("保存雨水情失败！");
        }
        return dutyFxkh;
    }

    @Override
    public DutyRecordTel saveDutyRecordTel(SaveDutyRecordTelVO sdrt) throws IllegalStateException, IOException {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Date now = new Date();
        DutyRecordTel dutyRecordTel = new DutyRecordTel();
        dutyRecordTel.setUpdateUser(user.getuserName());
        dutyRecordTel.setUpdateDate(now);
        dutyRecordTel.setId(sdrt.getId());
        dutyRecordTel.setTime(DateUtil.parse(sdrt.getTime()));
        dutyRecordTel.setType(sdrt.getType());
        dutyRecordTel.setUnits(sdrt.getUnits());
        dutyRecordTel.setTelephone(sdrt.getTelephone());
        dutyRecordTel.setContent(sdrt.getContent());
        dutyRecordTel.setAddvcd(user.getAddvcd());
        MultipartFile file = sdrt.getFile();
        if (file != null) {
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
        if (sdrt.getId() == null) {
            i = dutyRecordTelMapper.insert(dutyRecordTel);
        } else {
            i = dutyRecordTelMapper.updateById(dutyRecordTel);
        }
        if (i != 1) {
            throw new RuntimeException("保存来电记录失败！");
        }
        return dutyRecordTel;
    }

    @Override
    @Transactional
    public void delDutyRecordTel(Integer id) {
        int i = dutyRecordTelMapper.deleteById(id);
        if (i != 1) {
            throw new RuntimeException("删除失败！");
        }
    }

    @Override
    public DutyRecord getDutyRecordToday(Date time,String addvcd) {
        QueryWrapper<DutyRecord> qw = new QueryWrapper<>();
        qw.select().eq("time", time).eq("addvcd",addvcd);
        DutyRecord dutyRecord = dutyRecordMapper.selectOne(qw);
        return dutyRecord;
    }

    @Override
    public int saveDutyRecord(DutyRecord dutyRecord) {
        int res = 0;
        if (dutyRecord.getId() == null) {// 不存在记录 插入数据
            res = dutyRecordMapper.insert(dutyRecord);
        } else {// 存在记录 更新数据
            res = dutyRecordMapper.updateById(dutyRecord);
        }
        if (res > 0) {
            return dutyRecord.getId();
        } else {
            return 0;
        }
    }

    @Override
    public List<DutyOrgan> getDutyOrgan() {
        return dutyOrganMapper.selectList(null);
    }

    @Override
    public int handOver(String member, String memberOld) {

        return 0;
    }

    @Override
    public int updateDutyFlag(DutyRecord dutyRecord) {
        int res = 0;
        res = dutyRecordMapper.updateById(dutyRecord);
        if (res > 0){
            return dutyRecord.getId();
        }else {
            return 0;
        }
    }

    @Override
    public DutyRecord getDutyRecordById(String id) {
        DutyRecord dutyRecord = dutyRecordMapper.selectById(id);
        return dutyRecord;
    }

    @Override
    public DutyRecordTel getDutyRecordTelById(String id) {
        DutyRecordTel dutyRecordTel = dutyRecordTelMapper.selectById(id);
        return dutyRecordTel;
    }

    @Override
    public DutyRecordTel getTelFile(String addvcd, String date, MultipartFile file) throws IllegalStateException, IOException{
        Date now = new Date();
        DutyRecordTel dutyRecordTel = new DutyRecordTel();
        dutyRecordTel.setUpdateDate(now);
        dutyRecordTel.setAddvcd(addvcd);
        dutyRecordTel.setTime(DateUtil.parse(date));
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
        i = dutyRecordTelMapper.insert(dutyRecordTel);
        if (i != 1) {
            throw new RuntimeException("保存来电记录失败！");
        }

        return dutyRecordTel;
    }

}
