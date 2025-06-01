package com.siir.itq.events.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiErrorResponse {
    private int statusCode;
    private String status;
    private String message;
    private String timestamp;
    
    public ApiErrorResponse(int statusCode, String status, String message, String timestamp) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
