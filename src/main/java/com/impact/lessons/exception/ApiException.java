package com.impact.lessons.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int errorCode;

    public ApiException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

