package com.quyet.superapp.exception;

import java.util.Map;

public class RegistrationException extends RuntimeException {
    private final Map<String, String> errors;

    public RegistrationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
