package com.epam.gymapp.TrainerWorkloadService.converters;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class IntegerToYearConverterTest {
    @Test
    void shouldConvertIntegerToYear() {
        IntegerToYearConverter converter = new IntegerToYearConverter();

        Year result = converter.convert(2025);

        assertEquals(Year.of(2025), result);
    }
}