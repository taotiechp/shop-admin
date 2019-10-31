package com.fh.shop.api.exception;

import com.fh.shop.api.common.Enum;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ServerResponse ExceptinHeadller(GlobalException globalException){
        Enum anEnum = globalException.getAnEnum();
        return ServerResponse.error(anEnum);
    }

}
