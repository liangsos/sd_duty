package com.sd.modules.system.service.impl;

import com.sd.modules.system.entity.Log;
import com.sd.modules.system.mapper.LogMapper;
import com.sd.modules.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志服务类
 * @author Chen Hualiang
 * @create 2020-10-20 9:57
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public int insertLog(Log log) {
        int res = logMapper.insert(log);
        return res;
    }
}
