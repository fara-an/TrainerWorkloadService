package com.epam.gymapp.TrainerWorkloadService.model;

import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class YearSummaryTest {


    @Test
    void constructor_ShouldInitializeWithYearAndTrainerWorkload() {
        Year year = Year.of(2025);

        YearSummary yearSummary = new YearSummary(year);

        assertEquals(year, yearSummary.getYear());
        assertNotNull(yearSummary.getMonths());
        assertTrue(yearSummary.getMonths().isEmpty());
    }

    @Test
    void addMonthSummary_ShouldAddMonthAndSetBackReference() {
        YearSummary yearSummary = new YearSummary(Year.of(2025));
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY);

        yearSummary.addMonthSummary(monthSummary);

        assertTrue(yearSummary.getMonths().contains(monthSummary));
    }

    @Test
    void removeMonthSummary_ShouldRemoveMonthAndClearBackReference() {
        YearSummary yearSummary = new YearSummary(Year.of(2025));
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY);
        yearSummary.addMonthSummary(monthSummary);

        yearSummary.removeMonthSummary(monthSummary);

        assertFalse(yearSummary.getMonths().contains(monthSummary));
    }
}

