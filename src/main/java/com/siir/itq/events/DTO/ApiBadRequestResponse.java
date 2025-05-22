package com.siir.itq.events.DTO;

import java.util.Map;

public class ApiBadRequestResponse {
    private int status;
    private Map<String, String> errors;

    public ApiBadRequestResponse(int status, Map<String, String> errors) {
        this.status = status;
        this.errors = errors;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
