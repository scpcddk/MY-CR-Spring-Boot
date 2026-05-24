package com.example.myclashroyaleserver.monitor;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice//声明统一处理所有 Controller 抛出的异常
public class AiopsAnalyzer {
    //捕获到 IllegalArgumentException 时，执行这个方法
    @ExceptionHandler(IllegalArgumentException.class)//指定要捕获哪种类型的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)//表示返回给客户端的 HTTP 状态码
    public Map<String, Object> handleIllegalArg(IllegalArgumentException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", 400);
        error.put("message", e.getMessage());
        return error;
    }

    //捕获所有其他未指定类型的异常（兜底）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", 500);
        error.put("message", "Internal server error");
        return error;
    }
}
