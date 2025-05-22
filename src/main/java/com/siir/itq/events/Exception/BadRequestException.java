package com.siir.itq.events.Exception;

import java.util.Map;

public class BadRequestException extends RuntimeException {
    private Map<String, String> errors;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Map<String, String> errors) {
        super("Error de validación");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
