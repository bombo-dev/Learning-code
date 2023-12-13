package com.example.redis.exception;

public class DuplicateUrlException extends RuntimeException {

    public DuplicateUrlException(String message) {
        super(message);
    }
}
