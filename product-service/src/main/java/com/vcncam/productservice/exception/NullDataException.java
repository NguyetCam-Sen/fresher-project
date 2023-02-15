package com.vcncam.productservice.exception;

public class NullDataException extends CustomException{
    public NullDataException(String errorCode, String errorStatus) {
        super(errorCode, errorStatus);
    }
}
