package com.impact.lessons.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    AUTH_FAILED(401, "Authentication failed"),
    ACCESS_DENIED(403, "Insufficient permissions for this action"),
    USER_NOT_FOUND(404, "User does not exist"),
    TOKEN_EXPIRED(401, "Your session has expired");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

