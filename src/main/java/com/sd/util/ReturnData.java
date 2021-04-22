package com.sd.util;

import lombok.Data;

import java.util.List;

/**
 * 返回消息数据工具类
 * @author Chen Hualiang
 * @create 2020-10-10 10:24
 */
@Data
public class ReturnData<T> {
    private Integer code = 0;

    private Integer count;

    private List<T> data;

    private String msg = "";
}
