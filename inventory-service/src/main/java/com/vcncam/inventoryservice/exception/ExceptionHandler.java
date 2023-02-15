package com.vcncam.inventoryservice.exception;

import com.vcncam.inventoryservice.response.general.GeneralResponse;
import com.vcncam.inventoryservice.response.general.GeneralResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<GeneralResponse> exceptionHandler(CustomException e) {
        GeneralResponseStatus generalResponseStatus = GeneralResponseStatus.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();

        GeneralResponse generalResponse = GeneralResponse.builder()
                .status(generalResponseStatus)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(generalResponse);
    }
}
