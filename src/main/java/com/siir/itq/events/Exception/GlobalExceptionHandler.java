package com.siir.itq.events.Exception;

import com.siir.itq.events.DTO.ApiErrorResponse;
import com.siir.itq.events.DTO.ApiBadRequestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Corresponds to #/components/responses/ResourceNotFoundResponse which uses ResponseCode schema
        ApiErrorResponse errorResponse = new ApiErrorResponse("Error 404", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiBadRequestResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Corresponds to #/components/responses/BadRequestResponse
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
            .collect(Collectors.toMap(
                error -> ((FieldError) error).getField(),
                DefaultMessageSourceResolvable::getDefaultMessage,
                (existingValue, newValue) -> existingValue + "; " + newValue // In case of multiple errors for the same field
            ));
        
        ApiBadRequestResponse badRequestResponse = new ApiBadRequestResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(BadRequestException.class) // Custom BadRequestException if needed for other scenarios
    public ResponseEntity<ApiBadRequestResponse> handleBadRequestException(BadRequestException ex) {
        Map<String, String> errors = ex.getErrors() != null ? ex.getErrors() : Map.of("general", ex.getMessage());
        ApiBadRequestResponse badRequestResponse = new ApiBadRequestResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class) // For internal state issues like the one in ParticipanteEvento
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        // Corresponds to #/components/responses/ServerErrorResponse (generic server error)
        // Or could be a BadRequest if it's due to inconsistent client data that passed initial validation
        ApiErrorResponse errorResponse = new ApiErrorResponse("Error 400", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // Or HttpStatus.INTERNAL_SERVER_ERROR for true server issues
    }
    
    @ExceptionHandler(Exception.class) // Generic exception handler
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        // Corresponds to #/components/responses/ServerErrorResponse
        // Log the exception here for debugging
        ex.printStackTrace(); // For demonstration; use a proper logger in production
        ApiErrorResponse errorResponse = new ApiErrorResponse("Error 500", "Ocurrió un error inesperado en el servidor.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
