package com.epam.gymapp.TrainerWorkloadService.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.Year;

@ReadingConverter
public class IntegerToYearConverter implements Converter<Integer, Year> {
    @Override
    public Year convert(Integer source) {
        return Year.of(source);
    }
}
