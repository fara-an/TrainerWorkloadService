package com.epam.gymapp.TrainerWorkloadService.model;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class TrainerWorkloadTest {

    @Test
    void constructor_ShouldInitializeWithUsername() {
        TrainerWorkload trainerWorkload = new TrainerWorkload("john.doe");

        assertEquals("john.doe", trainerWorkload.getUsername());
        assertNotNull(trainerWorkload.getYears());
        assertTrue(trainerWorkload.getYears().isEmpty());
        assertFalse(trainerWorkload.isActive());
    }

    @Test
    void setters_ShouldUpdateFields() {
        TrainerWorkload trainer = new TrainerWorkload("alice");

        trainer.setFirstName("Alice");
        trainer.setLastName("Smith");
        trainer.setActive(true);

        assertEquals("Alice", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertTrue(trainer.isActive());
    }

    @Test
    void addYearSummary_ShouldAddYearAndSetBackReference() {
        TrainerWorkload trainer = new TrainerWorkload("bob");
        YearSummary yearSummary = new YearSummary(Year.of(2025), trainer);

        trainer.addYearSummary(yearSummary);

        assertTrue(trainer.getYears().contains(yearSummary));
        assertEquals(trainer, yearSummary.getTrainerWorkload());
    }
}
