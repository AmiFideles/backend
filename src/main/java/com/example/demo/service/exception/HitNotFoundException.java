package com.example.demo.service.exception;

public class HitNotFoundException extends RuntimeException{
    public HitNotFoundException(String message) {
        super(message);
    }

    public HitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
