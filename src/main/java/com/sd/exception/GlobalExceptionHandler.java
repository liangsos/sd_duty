package com.sd.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sd.util.RestResponse;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cn.hutool.json.JSONUtil;
import freemarker.template.TemplateModelException;

/**
 * 统一异常处理类
 * @author Chen Hualiang
 * @create 2020-10-09 15:28
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 500 - Bad Request
     */
    @ExceptionHandler({HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            TemplateModelException.class,
            SQLException.class})
    public void handleHttpMessageNotReadableException(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      Exception e){

        RestResponse restResponse = RestResponse.failure(e.getMessage());
        try {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(restResponse));
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
