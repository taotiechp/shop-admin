package com.fh.shop.api.exception;

import com.fh.shop.api.common.Enum;

public class GlobalException extends RuntimeException{

    private Enum anEnum;

    public GlobalException(Enum anEnum) {
        this.anEnum = anEnum;
    }

    public Enum getAnEnum(){
        return this.anEnum;
    }

}
