package com.epam.gymapp.TrainerWorkloadService.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class YearAttributeConverterTest {

    private YearAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new YearAttributeConverter();
    }

    @Test
    void testConvertToDatabaseColumn_withValidYear() {
        Year year = Year.of(2025);
        Short result = converter.convertToDatabaseColumn(year);
        assertEquals(Short.valueOf((short) 2025), result);
    }

    @Test
    void testConvertToDatabaseColumn_withNull() {
        Short result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute_withValidShort() {
        Short dbValue = 2025;
        Year result = converter.convertToEntityAttribute(dbValue);
        assertEquals(Year.of(2025), result);
    }

    @Test
    void testConvertToEntityAttribute_withNull() {
        Year result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute_withEdgeValues() {
        Year minYear = converter.convertToEntityAttribute(Short.MIN_VALUE);
        Year maxYear = converter.convertToEntityAttribute(Short.MAX_VALUE);

        assertEquals(Year.of(Short.MIN_VALUE), minYear);
        assertEquals(Year.of(Short.MAX_VALUE), maxYear);
    }
}
