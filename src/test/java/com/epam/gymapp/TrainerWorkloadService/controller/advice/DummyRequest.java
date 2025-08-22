package com.epam.gymapp.TrainerWorkloadService.controller.advice;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DummyRequest {
    @NotBlank(message = "must not be blank")
    private String name;
}
