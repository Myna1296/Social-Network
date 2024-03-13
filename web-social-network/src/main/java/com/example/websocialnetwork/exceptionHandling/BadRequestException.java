package com.example.websocialnetwork.exceptionHandling;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
