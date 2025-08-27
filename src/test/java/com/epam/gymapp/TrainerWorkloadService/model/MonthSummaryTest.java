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

    @Test
    void equals_ShouldReturnTrueForSameId() {
        MonthSummary m1 = new MonthSummary(Month.JANUARY, yearSummary);
        MonthSummary m2 = new MonthSummary(Month.JANUARY, yearSummary);

        m1.setId(1L);
        m2.setId(1L);

        assertEquals(m1, m2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentId() {
        MonthSummary m1 = new MonthSummary(Month.JANUARY, yearSummary);
        MonthSummary m2 = new MonthSummary(Month.JANUARY, yearSummary);

        m1.setId(1L);
        m2.setId(2L);

        assertNotEquals(m1, m2);
    }

    @Test
    void equals_ShouldReturnFalseWhenIdIsNull() {
        MonthSummary m1 = new MonthSummary(Month.JANUARY, yearSummary);
        MonthSummary m2 = new MonthSummary(Month.JANUARY, yearSummary);

        m1.setId(null);
        m2.setId(2L);

        assertNotEquals(m1, m2);
    }

    @Test
    void hashCode_ShouldBeBasedOnClass() {
        MonthSummary m1 = new MonthSummary(Month.JANUARY, yearSummary);
        MonthSummary m2 = new MonthSummary(Month.FEBRUARY, yearSummary);

        assertEquals(m1.hashCode(), m2.hashCode());
    }
}
