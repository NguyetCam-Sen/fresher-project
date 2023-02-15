package com.vcncam.orderservice.exception;

import com.vcncam.orderservice.response.general.GeneralResponse;
import com.vcncam.orderservice.response.general.GeneralResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

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

    @org.springframework.web.bind.annotation.ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<GeneralResponse> exceptionHandler(OrderNotFoundException e) {
        GeneralResponseStatus generalResponseStatus = GeneralResponseStatus.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();

        GeneralResponse generalResponse = GeneralResponse.builder()
                .status(generalResponseStatus)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(generalResponse, HttpStatus.NOT_FOUND);
    }

//    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<GeneralResponse> handleValidationException(MethodArgumentNotValidException e) {
//        List<String> errors = e.getBindingResult().getFieldErrors()
//                .stream().map(FieldError::getDefaultMessage).toList();
//
//        GeneralResponseStatus generalResponseStatus = GeneralResponseStatus
//                .builder()
//                .code("004")
//                .message("Validation Error Occurred")
//                .build();
//
//        GeneralResponse<Object> generalResponse = GeneralResponse
//                .builder()
//                .status(generalResponseStatus)
//                .timestamp(LocalDateTime.now())
//                .data(errors)
//                .build();
//
//        return new ResponseEntity<>(generalResponse, HttpStatus.BAD_REQUEST);
//    }
}
