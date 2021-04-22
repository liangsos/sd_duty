package com.sd.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * @author Chen Hualiang
 * @create 2020-10-13 10:26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
