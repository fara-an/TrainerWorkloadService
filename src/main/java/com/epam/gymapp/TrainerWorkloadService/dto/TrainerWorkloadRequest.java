package com.epam.gymapp.TrainerWorkloadService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TrainerWorkloadRequest {

    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private boolean isActive;
    @NotNull
    private LocalDateTime trainingDate;
    @Positive
    private Integer duration;
    @NotNull
    private ActionType actionType;


}
