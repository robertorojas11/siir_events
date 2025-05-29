package com.siir.itq.events.config.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.siir.itq.events.config.exceptions.CustomExceptions.*;
import com.siir.itq.events.config.exceptions.dto.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({InternalServerError.class, ResourceNotFoundException.class
        , ResourceConflictException.class, NotAuthorizedException.class})
    public ResponseEntity<?> runtimeException(RuntimeException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();

        if(ex instanceof ResourceNotFoundException){
            status = HttpStatus.NOT_FOUND;
        }else if(ex instanceof ResourceConflictException){
            status = HttpStatus.CONFLICT; 
        }else if(ex instanceof NotAuthorizedException){
            status = HttpStatus.UNAUTHORIZED;
        }else{
            message = "A internal server error has ocurred";
        }

        ErrorResponse response = new ErrorResponse(status.value(), status, message);
        
        return ResponseEntity.status(status).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationRequestExceptions(MethodArgumentNotValidException e){
        HashMap<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach( error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            }
        );

        return new ResponseEntity<>(Map.of(
            "status", HttpStatus.BAD_REQUEST.value(), 
            "errors", errors
        ), HttpStatus.BAD_REQUEST);
    }

}
