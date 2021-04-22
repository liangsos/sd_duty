package com.sd.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 数据Entity类
 * @author Chen Hualiang
 * @create 2020-10-12 9:39
 */
@Data
public abstract class TreeEntity extends DataEntity{
    private static final long serialVersionUID = 1L;

    /**
     * varchar(64) NULL父id
     */
    @TableField(value = "parent_id")
    protected Long parentId;

    /**
     * 节点层次（第一层，第二层，第三层....）
     */
    protected Integer level;

    /**
     * varchar(1000) NULL路径
     */
    @TableField(value = "parent_ids")
    protected String parentIds;

    /**
     * int(11) NULL排序
     */
    protected Integer sort;

    @TableField(exist = false)
    protected List children;

//    @TableField(exist = false)
//    protected T parentTree;

    public TreeEntity() {
        super();
        this.sort = 30;
    }

    public TreeEntity(Long id) {
        super(id);
    }

}
