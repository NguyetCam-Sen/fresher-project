package com.vcncam.productservice.controller;

import com.vcncam.productservice.response.general.GeneralResponse;
import com.vcncam.productservice.response.general.GeneralResponseStatus;

import java.time.LocalDateTime;

public class BaseController<T> {

    public GeneralResponse<T> success(T object) {
        GeneralResponseStatus generalResponseStatus = GeneralResponseStatus.builder()
                .code("200")
                .message("Success")
                .build();

        return (GeneralResponse<T>) GeneralResponse.builder()
                .status(generalResponseStatus)
                .timestamp(LocalDateTime.now())
                .data(object)
                .build();
    }

    public GeneralResponse<Object> success(String message) {
        GeneralResponseStatus generalResponseStatus = GeneralResponseStatus.builder()
                .code("000")
                .message(message)
                .build();

        return GeneralResponse.builder()
                .status(generalResponseStatus)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
