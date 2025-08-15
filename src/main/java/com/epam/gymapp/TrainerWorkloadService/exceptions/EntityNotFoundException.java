package com.epam.gymapp.TrainerWorkloadService.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public String getUserMessage() {
        return "Error occurred during database interaction.";
    }

}
