package com.epam.gymapp.TrainerWorkloadService.cucumber.integration.steps.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponse {
    private String message;
    private String token;
}
