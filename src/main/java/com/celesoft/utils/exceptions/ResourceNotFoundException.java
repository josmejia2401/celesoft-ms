package com.celesoft.utils.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
    private HttpStatus status;
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public ResourceNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
