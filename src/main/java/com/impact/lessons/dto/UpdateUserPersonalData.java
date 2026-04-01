package com.impact.lessons.dto;


import lombok.Data;


import java.time.LocalDate;


@Data
public class UpdateUserPersonalData {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}
