package com.siir.itq.events.DTO;

public class ApiErrorResponse {
    private String message;
    private String reason;

    public ApiErrorResponse(String reason, String message) {
        this.message = message;
        this.reason = reason;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}
