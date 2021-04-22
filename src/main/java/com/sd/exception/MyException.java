package com.sd.exception;

import lombok.Data;

/**
 * 自定义异常
 * @author Chen Hualiang
 * @create 2020-10-09 15:29
 */
@Data
public class MyException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private String msg;

    private int code;

    public MyException() {
        this.code = 500;
    }

    public MyException(String msg,Throwable cause) {
        super(msg,cause);
        this.msg = msg;
    }
    public MyException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public MyException(String msg, int code,Throwable cause) {
        super(msg,cause);
        this.msg = msg;
        this.code = code;
    }
    public MyException(String msg) {
        this.code = 500;
        this.msg = msg;
    }

}
