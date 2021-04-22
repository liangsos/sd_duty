package com.sd.base;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * Entity基础类
 * @author Chen Hualiang
 * @create 2020-09-29 15:28
 */
public abstract class BaseEntity extends Model {
    /**
     *实体编号(唯一标志)
     */
    protected Long id;

    public BaseEntity() {
    }

    public BaseEntity(Long id) {
        this.id = id;
    }

    @JsonSerialize(using= ToStringSerializer.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntity that = (BaseEntity) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }
}
