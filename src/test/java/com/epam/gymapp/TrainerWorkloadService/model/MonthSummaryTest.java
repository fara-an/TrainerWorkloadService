package com.epam.gymapp.TrainerWorkloadService.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class MonthSummaryTest {

    private YearSummary yearSummary;

    @BeforeEach
    void setUp() {
        yearSummary = new YearSummary();
    }

    @Test
    void constructor_ShouldInitializeWithZeroDuration() {
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY, yearSummary);

        assertEquals(Month.JANUARY, monthSummary.getMonth());
        assertEquals(yearSummary, monthSummary.getYearSummary());
        assertEquals(0, monthSummary.getTotalDuration());
    }

    @Test
    void addDuration_ShouldIncreaseTotalDuration() {
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY, yearSummary);

        monthSummary.addDuration(60);
        monthSummary.addDuration(30);

        assertEquals(90, monthSummary.getTotalDuration());
    }

    @Test
    void subtractDuration_ShouldDecreaseTotalDuration() {
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY, yearSummary);
        monthSummary.addDuration(100);

        monthSummary.subtractDuration(40);

        assertEquals(60, monthSummary.getTotalDuration());
    }
}

