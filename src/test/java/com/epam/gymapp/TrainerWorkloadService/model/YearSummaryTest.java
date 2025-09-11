package com.epam.gymapp.TrainerWorkloadService.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class YearSummaryTest {

    private TrainerWorkload trainerWorkload;

    @BeforeEach
    void setUp() {
        trainerWorkload = new TrainerWorkload();
    }

    @Test
    void constructor_ShouldInitializeWithYearAndTrainerWorkload() {
        Year year = Year.of(2025);

        YearSummary yearSummary = new YearSummary(year, trainerWorkload);

        assertEquals(year, yearSummary.getYear());
        assertEquals(trainerWorkload, yearSummary.getTrainerWorkload());
        assertNotNull(yearSummary.getMonths());
        assertTrue(yearSummary.getMonths().isEmpty());
    }

    @Test
    void addMonthSummary_ShouldAddMonthAndSetBackReference() {
        YearSummary yearSummary = new YearSummary(Year.of(2025), trainerWorkload);
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY, yearSummary);

        yearSummary.addMonthSummary(monthSummary);

        assertTrue(yearSummary.getMonths().contains(monthSummary));
        assertEquals(yearSummary, monthSummary.getYearSummary());
    }

    @Test
    void removeMonthSummary_ShouldRemoveMonthAndClearBackReference() {
        YearSummary yearSummary = new YearSummary(Year.of(2025), trainerWorkload);
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY, yearSummary);
        yearSummary.addMonthSummary(monthSummary);

        yearSummary.removeMonthSummary(monthSummary);

        assertFalse(yearSummary.getMonths().contains(monthSummary));
        assertNull(monthSummary.getYearSummary());
    }
}

