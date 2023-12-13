package com.example.redis.exception;

public class ExceedUrlRequestException extends RuntimeException {

    public ExceedUrlRequestException(String message) {
        super(message);
    }
}
