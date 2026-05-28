package com.soap.envmonitorsystem2.controller;

import com.soap.envmonitorsystem2.dto.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ApiResult<Void> handleBadRequest(RuntimeException ex) {
        return ApiResult.fail(ex.getMessage());
    }
}
