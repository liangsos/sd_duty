package com.sd.config;

import com.sd.base.MyHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;

/**
 * springMVC配置文件
 * @author Chen Hualiang
 * @create 2020-10-12 10:18
 */
@Configuration
public class DutyWebMvcConfigurer implements WebMvcConfigurer {
//    @Value("${duty.allowedOrigins}")
//    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                //放行哪些原始域
                .allowedOrigins("*")
                //放行哪些请求方式
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                //是否发送Cookie信息
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Authorization,Token");

//        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowCredentials(true).maxAge(3600).allowedMethods("*");
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/login","/api/systemLogout","/api/dutyQuery/getDutyBb","/api/getDuty","/api/file/GetDutyDoc",
                        "/api/file/GetDutyMat","/api/file/GetDutyAnno","/api/email/getEmails","/api/communication/getCommunication","/api/getDutyForDp",
                        "/api/phone/getCommunication","/api/phone/getDutyBb","/api/getAddvcdStcd","/api/getCommunicationNew");

    }
}
