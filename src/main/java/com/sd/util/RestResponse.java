package com.sd.util;

import java.util.HashMap;

/**
 * ResponseBody构造器。一般用于ajax、rest等类型的Web服务
 * @author Chen Hualiang
 * @create 2020-10-09 15:37
 */
public class RestResponse extends HashMap<String,Object> {

    /**
     *
     */
    private static final long serialVersionUID = 7881713497464166778L;

    public static RestResponse success() {
        return success("成功");
    }

    public static RestResponse success(String message){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(true);
        restResponse.setMessage(message);
        restResponse.setCode(0);
        return restResponse;
    }

    public static RestResponse failure(String message){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(false);
        restResponse.setMessage(message);
        restResponse.setCode(1);
        return restResponse;
    }

    public RestResponse setSuccess(Boolean success) {
        if (success != null) put("success", success);
        return this;
    }

    public RestResponse setMessage(String message) {
        if (message != null) put("message", message);
        return this;
    }

    public RestResponse setData(Object data) {
        if (data != null) put("data", data);
        return this;
    }

    public RestResponse setCode(Object code) {
        if (code != null) put("code", code);
        return this;
    }

    public RestResponse setPage(Integer page) {
        if (page != null) put("page", page);
        return this;
    }

    public RestResponse setCurrentPage(Integer currentPage){
        if (currentPage != null) put("currentPage", currentPage);
        return this;
    }

    public RestResponse setLimit(Integer limit) {
        if (limit != null) put("limit", limit);
        return this;
    }

    public RestResponse setTotal(Long total) {
        if (total != null) put("total", total);
        return this;
    }

    public RestResponse setAny(String key, Object value) {
        if (key != null && value != null) put(key, value);
        return this;
    }
}
