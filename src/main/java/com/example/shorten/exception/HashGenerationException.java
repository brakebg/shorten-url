package com.example.shorten.exception;

public class HashGenerationException extends RuntimeException {
    public HashGenerationException(String message) {
        super(message);
    }

    public HashGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
