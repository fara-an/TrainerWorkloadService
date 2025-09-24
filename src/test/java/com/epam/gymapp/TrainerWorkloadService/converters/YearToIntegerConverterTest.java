package com.epam.gymapp.TrainerWorkloadService.converters;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class YearToIntegerConverterTest {

    @Test
    void shouldConvertYearToInteger() {
        Year year = Year.of(2025);
        YearToIntegerConverter converter = new YearToIntegerConverter();

        Integer result = converter.convert(year);

        assertEquals(2025, result);
    }
}