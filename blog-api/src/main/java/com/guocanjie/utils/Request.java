package com.guocanjie.utils;

import lombok.Data;

@Data
public class Request {

    private Integer code;
    private String msg;
    private Object data;
    private Boolean success;

    public  Request(Integer code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Request(Object data){
        this.success = true;
        this.code = 200;
        this.msg = "请求成功";
        this.data = data;
    }

}
