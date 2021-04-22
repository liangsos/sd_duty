package com.sd.base;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sd.util.Constants;
import com.sd.util.RestResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.json.JSONUtil;

/**
 * 自定义shiro表单拦截器
 * @author Chen Hualiang
 * @create 2020-10-10 15:24
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);

    /**
     * 功能描述：解决跨域问题，放行OPTIONS请求
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletResponse res = (HttpServletResponse)response;
//        res.setHeader("Access-Control-Allow-Origin", Constants.ALLOWEDORIGINS);
        res.setHeader("Access-Control-Allow-Origin", "*");
//        res.setHeader("Access-Control-Allow-Methods","GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH");
//        res.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type,Accept, Authorization");
        res.setHeader("Access-Control-Allow-Credentials","true");
        if(request instanceof HttpServletRequest){
            if(((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")){
                res.addHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH");
                res.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
                res.setStatus(HttpServletResponse.SC_OK);
                res.setCharacterEncoding("UTF-8");
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 功能描述：用户未登录返回信息
     * @param request
     * @param response
     * @return
     * @throws Exception
     * 系统重定向会默认把请求头清空，这里通过拦截器重新设置请求头，解决跨域问题
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
//        res.setHeader("Access-Control-Allow-Origin", Constants.ALLOWEDORIGINS);
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("UTF-8");
        RestResponse restResponse = RestResponse.failure("未登录").setCode(-1);
        response.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = res.getWriter();
        writer.write(JSONUtil.toJsonStr(restResponse));
        writer.close();
        return false;
    }
}
