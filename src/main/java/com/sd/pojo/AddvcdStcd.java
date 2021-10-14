package com.sd.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Addvcd_Stcd")
@Data
public class AddvcdStcd {
    @TableId("addvcd")
    private String addvcd;

    @TableField("addvnm")
    private String addvnm;

    @TableField("city")
    private String city;
}
