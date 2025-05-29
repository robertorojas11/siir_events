package com.siir.itq.events.config.exceptions.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
    Integer statusCode,
    HttpStatus status,
    String message, 
    LocalDateTime timestamp
) {

    public ErrorResponse(Integer statusCode, HttpStatus status, String message){
        this(statusCode, status, message, LocalDateTime.now());
    }   
}
