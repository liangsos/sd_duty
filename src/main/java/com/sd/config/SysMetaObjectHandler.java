package com.sd.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sd.base.MySysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatisplus自定义填充公共字段 ,即没有传的字段自动填充
 * @author Chen Hualiang
 * @create 2020-10-12 10:33
 */
@Component
@Slf4j
public class SysMetaObjectHandler implements MetaObjectHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("正在调用该insert填充字段方法");
        Object updateDate = getFieldValByName("updateDate",metaObject);
        Object updateId = getFieldValByName("updateId",metaObject);
        Object updateUser = getFieldValByName("updateUser",metaObject);
        Object createTime = getFieldValByName("createTime",metaObject);
        if (null == updateDate) {
            setFieldValByName("updateDate", new Date(),metaObject);
        }
        if (null == updateId) {
            if(MySysUser.ShiroUser() != null) {
                setFieldValByName("updateId", MySysUser.id(), metaObject);
            }
        }
        if (null == updateUser){
            if (MySysUser.ShiroUser() != null){
                setFieldValByName("updateUser",MySysUser.userName(),metaObject);
            }
        }
        if (null == createTime) {
            setFieldValByName("createTime", new Date(),metaObject);
        }
    }

    //更新填充
    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("正在调用该update填充字段方法");
        setFieldValByName("updateDate",new Date(), metaObject);
        Object updateId = getFieldValByName("updateId",metaObject);
        if (null == updateId) {
            setFieldValByName("updateUser", MySysUser.userName(), metaObject);
        }
    }
}
