package com.guocanjie.handle;

import com.guocanjie.utils.Request;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AllExceptionHandle {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Request interceptAllException(Exception e){
        e.printStackTrace();
        return new Request(404,"系统异常",null);
    }
}
