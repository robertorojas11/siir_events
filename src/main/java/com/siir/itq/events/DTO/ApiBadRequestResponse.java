package com.siir.itq.events.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ApiBadRequestResponse {
    private int status; // HTTP status code
    private Map<String, String> errors;

    public ApiBadRequestResponse(int status, Map<String, String> errors) {
        this.status = status;
        this.errors = errors;
    }
}
