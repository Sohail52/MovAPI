package com.example.internintelligence_movieapidevelopment.exception;

public class TmdbServiceNotAvailableException extends RuntimeException {
    public TmdbServiceNotAvailableException(String message) {
        super(message);
    }

    public TmdbServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
