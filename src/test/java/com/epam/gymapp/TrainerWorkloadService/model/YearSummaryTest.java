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

    @Test
    void equals_ShouldReturnTrueForSameId() {
        YearSummary y1 = new YearSummary(Year.of(2025), trainerWorkload);
        YearSummary y2 = new YearSummary(Year.of(2025), trainerWorkload);

        y1.setId(1L);
        y2.setId(1L);

        assertEquals(y1, y2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentIds() {
        YearSummary y1 = new YearSummary(Year.of(2025), trainerWorkload);
        YearSummary y2 = new YearSummary(Year.of(2025), trainerWorkload);

        y1.setId(1L);
        y2.setId(2L);

        assertNotEquals(y1, y2);
    }

    @Test
    void equals_ShouldReturnFalseWhenIdIsNull() {
        YearSummary y1 = new YearSummary(Year.of(2025), trainerWorkload);
        YearSummary y2 = new YearSummary(Year.of(2025), trainerWorkload);

        y1.setId(null);
        y2.setId(2L);

        assertNotEquals(y1, y2);
    }

    @Test
    void hashCode_ShouldBeBasedOnClass() {
        YearSummary y1 = new YearSummary(Year.of(2025), trainerWorkload);
        YearSummary y2 = new YearSummary(Year.of(2026), trainerWorkload);

        assertEquals(y1.hashCode(), y2.hashCode());
    }
}
