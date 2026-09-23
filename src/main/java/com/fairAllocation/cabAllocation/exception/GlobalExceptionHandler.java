package com.fairAllocation.cabAllocation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {

        Map<String,Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("Timestamp", LocalDateTime.now());
        errorDetails.put("status",HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error","Business Logic Violation");
        errorDetails.put("message",ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
