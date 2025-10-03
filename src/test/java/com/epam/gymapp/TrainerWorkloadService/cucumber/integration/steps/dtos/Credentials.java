package com.epam.gymapp.TrainerWorkloadService.cucumber.integration.steps.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;

}
