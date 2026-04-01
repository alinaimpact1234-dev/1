package com.impact.lessons.dto;


import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;


@Setter
@Getter
public class UpdateBirthDateRequest {
    private LocalDate birthDate;


    public UpdateBirthDateRequest() {
    }


    public UpdateBirthDateRequest(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


}

