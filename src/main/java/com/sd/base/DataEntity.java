package com.sd.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 数据Entity类
 * @author Chen Hualiang
 * @create 2020-09-29 16:18
 */
@Data
public abstract class DataEntity extends BaseEntity  {
    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    protected Long createId;

    /**
     * 创建日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_date",fill = FieldFill.INSERT)
    protected Date createDate;

    /**
     * 更新者
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    protected Long updateId;

    /**
     * 更新日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_date",fill = FieldFill.INSERT_UPDATE)
    protected Date updateDate;

    /**
     * 删除标记（Y：正常；N：删除；A：审核；）
     */
    @TableField(value = "del_flag")
    protected Boolean delFlag;

    /**
     * 备注
     */
//    @TableField(strategy= FieldStrategy.IGNORED)
//    protected String remarks;

    /**
     * 创建着
     */
//    @TableField(exist = false)
//    protected User createUser;

    /**
     * 修改者
     */
//    @TableField(exist = false)
//    protected User updateUser;

    public DataEntity() {
        super();
        this.delFlag = false;
    }

    public DataEntity(Long id) {
        super(id);
    }


}
