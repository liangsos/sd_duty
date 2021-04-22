package com.sd.modules.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sd.modules.system.mapper.*;
import com.sd.modules.system.service.DutyFileService;
import com.sd.pojo.*;
import com.sd.realm.AuthRealm;
import com.sd.realm.AuthRealm.ShiroUser;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class DutyFileServiceImpl implements DutyFileService {

    @Value("${duty.file-root-path}")
    private String fileRootPath;
    @Value("${duty.file-host}")
    private String fileHost;

    @Autowired
    private DutyConsultMapper dutyConsultMapper;
    @Autowired
    private DutyDictMapper dutyDictMapper;
    @Autowired
    private DutyDocMapper dutyDocMapper;
    @Autowired
    private DutyMatMapper dutyMatMapper;
    @Autowired
    private DutyAnnoMapper dutyAnnoMapper;

    @Override
    public DutyConsult getDutyConsultById(Integer id) {
        DutyConsult dutyConsult = dutyConsultMapper.selectById(id);
        dutyConsult.setConslusion(fileHost + dutyConsult.getConslusion());
        return dutyConsult;
    }

    @Override
    public List<DutyDict> getDutyDict(String type) {
        QueryWrapper<DutyDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type).orderByAsc("dabh");
        List<DutyDict> list = dutyDictMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<DutyConsult> getDutyConsult(String beginTime, String endTime, long current, long size) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        List<DutyConsult> list = dutyConsultMapper.getDutyConsult(beginTime,endTime,current,size,addvcd);
        return list;
    }

    @Override
    public List<DutyDoc> getDutyDoc(String fileType,String beginTime, String endTime,  long current, long size,String isHome) {
        String addvcd;
        if(isHome.equals("1")){
            addvcd = "37";
        }else {
            AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            addvcd = user.getAddvcd();
        }
        List<DutyDoc> list = dutyDocMapper.getDutyDoc(fileType,beginTime,endTime,current,size,addvcd);
        return list;
    }

    @Override
    public DutyConsult saveDutyConsult(DutyConsult dutyConsult) throws IllegalStateException, IOException {
        Date now = new Date();
        ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Integer id = dutyConsult.getId();
        int i = 0;
        dutyConsult.setUpdateUser(u.getuserName());
        dutyConsult.setUpdateDate(now);
        dutyConsult.setAddvcd(u.getAddvcd());
        MultipartFile file = dutyConsult.getFile();
        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            String path = "consult-files/";
            String newFileName = DateUtil.format(now, DatePattern.PURE_DATETIME_PATTERN)
                    + originalFileName.substring(originalFileName.lastIndexOf("."));
            File f = new File(fileRootPath + path, newFileName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
//            FileUtils.copyInputStreamToFile();
            file.transferTo(f);
            dutyConsult.setConslusion(path + newFileName);
            dutyConsult.setDocment(originalFileName);
        }
        if (id == null) {
            i = dutyConsultMapper.insert(dutyConsult);
        } else {
            i = dutyConsultMapper.updateById(dutyConsult);
        }
        if (i != 1) {
            throw new RuntimeException("保存失败");
        }
        return dutyConsult;
    }

    @Override
    public DutyDoc saveDutyDoc(DutyDoc dutyDoc) throws IllegalStateException, IOException {
        Integer id = dutyDoc.getId();
        MultipartFile file = dutyDoc.getFile();
        ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        dutyDoc.setAddvcd(u.getAddvcd());
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String path = "shareDoc-files/";
            String newFileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN)
                    + originalFileName.substring(originalFileName.lastIndexOf("."));
            File f = new File(fileRootPath + path, newFileName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            file.transferTo(f);
            dutyDoc.setSource(fileHost + path + newFileName);
            dutyDoc.setFileName(originalFileName);
        }
        int i = 0;
        if (id == null) {
            i = dutyDocMapper.insert(dutyDoc);
        } else {
            i = dutyDocMapper.updateById(dutyDoc);
        }
        if (i != 1) {
            throw new RuntimeException("保存失败");
        }
        return dutyDoc;
    }

    @Override
    public int getDutyConsultCount(String beginTime, String endTime) {
        AuthRealm.ShiroUser user = (AuthRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String addvcd = user.getAddvcd();
        int count = dutyConsultMapper.getDutyConsultCount(beginTime,endTime,addvcd);
        return count;
    }

    @Override
    public boolean delDutyConsult(String id) {
        boolean flag = false;
        int res = dutyConsultMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public int getDutyDocCount(String fileType, String beginTime, String endTime,String isHome) {
        String addvcd;
        if (isHome.equals("1")){
            addvcd = "37";
        }else {
            ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            addvcd = u.getAddvcd();
        }
        int count = dutyDocMapper.getDutyDocCount(fileType,beginTime,endTime,addvcd);
        return count;
    }

    @Override
    public DutyDoc getDutyDocById(String id) {
        DutyDoc dutyDoc = dutyDocMapper.selectById(id);
        return dutyDoc;
    }

    @Override
    public boolean delDutyDoc(String id) {
        boolean flag = false;
        int res = dutyDocMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<DutyMat> getDutyMat(String beginTime, String endTime, int current, int size,String isHome) {
        String addvcd;
        if (isHome.equals("1")){
            addvcd = "37";
        }else {
            ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            addvcd = user.getAddvcd();
        }
        List<DutyMat> list = dutyMatMapper.getDutyMat(beginTime,endTime,current,size,addvcd);
        return list;
    }

    @Override
    public int getDutyMatCount(String beginTime, String endTime,String isHome) {
        String addvcd;
        if (isHome.equals("1")){
            addvcd = "37";
        }else {
            ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            addvcd = user.getAddvcd();
        }
        int count = dutyMatMapper.getDutyMatCount(beginTime,endTime,addvcd);
        return count;
    }

    @Override
    public DutyMat saveDutyMat(DutyMat dutyMat) throws IllegalStateException, IOException{
        Integer id = dutyMat.getId();
        MultipartFile file = dutyMat.getFile();
        ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        dutyMat.setAddvcd(u.getAddvcd());
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String path = "shareMat-files/";
            String newFileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN)
                    + originalFileName.substring(originalFileName.lastIndexOf("."));
            File f = new File(fileRootPath + path, newFileName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            file.transferTo(f);
            dutyMat.setSource(fileHost + path + newFileName);
            dutyMat.setFileName(originalFileName);
        }
        int i = 0;
        if (id == null) {
            i = dutyMatMapper.insert(dutyMat);
        } else {
            i = dutyMatMapper.updateById(dutyMat);
        }
        if (i != 1) {
            throw new RuntimeException("保存失败");
        }

        return dutyMat;
    }

    @Override
    public DutyMat getDutyMatById(String id) {
        DutyMat dutyMat = dutyMatMapper.selectById(id);
        return dutyMat;
    }

    @Override
    public boolean delDutyMat(String id) {
        boolean flag = false;
        int res = dutyMatMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public DutyAnno saveDutyAnno(DutyAnno dutyAnno) throws IllegalStateException, IOException{
        Integer id = dutyAnno.getId();
        MultipartFile file = dutyAnno.getFile();
        ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        dutyAnno.setAddvcd(u.getAddvcd());
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String path = "shareAnno-files/";
            String newFileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN)
                    + originalFileName.substring(originalFileName.lastIndexOf("."));
            File f = new File(fileRootPath + path, newFileName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            file.transferTo(f);
            dutyAnno.setSource(fileHost + path + newFileName);
            dutyAnno.setFileName(originalFileName);
        }
        int i = 0;
        if (id == null) {
            i = dutyAnnoMapper.insert(dutyAnno);
        } else {
            i = dutyAnnoMapper.updateById(dutyAnno);
        }
        if (i != 1) {
            throw new RuntimeException("保存失败");
        }

        return dutyAnno;
    }

    @Override
    public List<DutyAnno> getDutyAnno(String beginTime, String endTime, int current, int size,String isHome) {
        String addvcd;
        if (isHome.equals("1")){
            addvcd = "37";
        }else {
            ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            addvcd = user.getAddvcd();
        }
        List<DutyAnno> list = dutyAnnoMapper.getDutyAnno(beginTime,endTime,current,size,addvcd);
        return list;
    }

    @Override
    public int getDutyAnnoCount(String beginTime, String endTime,String isHome) {
        String addvcd;
        if (isHome.equals("1")){
            addvcd = "37";
        }else {
            ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            addvcd = user.getAddvcd();
        }
        int count = dutyAnnoMapper.getDutyAnnoCount(beginTime,endTime,addvcd);
        return count;
    }

    @Override
    public DutyAnno getDutyAnnoById(String id) {
        DutyAnno dutyAnno = dutyAnnoMapper.selectById(id);
        return dutyAnno;
    }

    @Override
    public boolean delDutyAnno(String id) {
        boolean flag = false;
        int res = dutyAnnoMapper.deleteById(id);
        if (res > 0){
            flag = true;
        }
        return flag;
    }

}
