package com.epam.gymapp.TrainerWorkloadService.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.Year;

@WritingConverter
public class YearToIntegerConverter implements Converter<Year, Integer> {
    @Override
    public Integer convert(Year source) {
        return source.getValue();
    }
}

