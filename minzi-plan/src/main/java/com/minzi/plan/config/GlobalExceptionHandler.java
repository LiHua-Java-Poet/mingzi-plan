package com.minzi.plan.config;

import com.minzi.common.core.R;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Exception>> handleAllExceptions(Exception ex) {
        ex.printStackTrace();
        // 返回统一的错误响应
        return new ResponseEntity<>(R.fail(406,ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}