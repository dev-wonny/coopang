package com.coopang.gateway.error;

public class JwtValidationException extends RuntimeException {
    private final String message;

    public JwtValidationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}