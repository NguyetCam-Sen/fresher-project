package com.vcncam.productservice.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException{
    private String errorCode;
    public CustomException(String errorCode, String errorStatus) {
        super(errorStatus);
        this.errorCode = errorCode;
    }
}
