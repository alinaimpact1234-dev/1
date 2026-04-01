package com.impact.lessons.dto;


import lombok.Data;


@Data
public class SetEmailRequest {
    private Long userId;
    private String email;
    private Boolean isPrimary;
}
