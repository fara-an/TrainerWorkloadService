package com.epam.gymapp.TrainerWorkloadService.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class MonthConverterTest {

    private MonthConverter converter;

    @BeforeEach
    void setUp() {
        converter = new MonthConverter();
    }

    @Test
    void testConvertToDatabaseColumn_withValidMonth() {
        String result = converter.convertToDatabaseColumn(Month.JANUARY);
        assertEquals("JANUARY", result);
    }

    @Test
    void testConvertToDatabaseColumn_withNull() {
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute_withValidString() {
        Month result = converter.convertToEntityAttribute("FEBRUARY");
        assertEquals(Month.FEBRUARY, result);
    }

    @Test
    void testConvertToEntityAttribute_withNull() {
        Month result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute_withInvalidString_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> converter.convertToEntityAttribute("NOT_A_MONTH"));
    }
}
