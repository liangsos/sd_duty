package com.sd.util;

/**
 * 常量类
 * @author Chen Hualiang
 * @create 2020-10-10 10:19
 */
public class Constants {
    /**
     * shiro采用加密算法
     */
    public static final String HASH_ALGORITHM = "md5";

    /**
     * 生成Hash值的迭代次数
     */
    public static final int HASH_INTERATIONS = 1;

    /**
     * 验证码
     */
    public static final String VALIDATE_CODE = "validateCode";

    /**
     * 生成盐的长度
     */
    public static final int SALT_SIZE = 8;

    /**
     *系统用户默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 允许跨域请求路径
     */
    public static final String ALLOWEDORIGINS = "http://127.0.0.1:5500";
}
