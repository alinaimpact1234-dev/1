package com.impact.lessons.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int errorCode;
    private String message;
    private long timestamp;
}

