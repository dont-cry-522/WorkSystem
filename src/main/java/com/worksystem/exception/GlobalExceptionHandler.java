package com.worksystem.exception;

import com.worksystem.dto.response.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResponse<?> handleBusinessException(BusinessException e) {
        return CommonResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e) {
        return CommonResponse.error(500, "系统异常");
    }
}