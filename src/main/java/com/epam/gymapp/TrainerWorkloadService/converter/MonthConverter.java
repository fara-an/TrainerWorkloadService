package com.epam.gymapp.TrainerWorkloadService.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Month;
@Converter(autoApply = true)
public class MonthConverter implements AttributeConverter<Month, String> {
    @Override
    public String convertToDatabaseColumn(Month month) {
        return (month == null) ? null : month.name();
    }

    @Override
    public Month convertToEntityAttribute(String month) {
        return month == null ? null : Month.valueOf(month);
    }
}
