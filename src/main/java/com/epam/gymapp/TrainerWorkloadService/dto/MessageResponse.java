package com.epam.gymapp.TrainerWorkloadService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MessageResponse {
    private String message;
}
