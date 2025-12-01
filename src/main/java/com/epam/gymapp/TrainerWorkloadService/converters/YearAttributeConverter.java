package com.epam.gymapp.TrainerWorkloadService.converters;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Year;

public class YearAttributeConverter implements AttributeConverter<Year> {
    
    @Override
    public AttributeValue transformFrom(Year input) {
        return AttributeValue.builder().n(String.valueOf(input.getValue())).build();
    }

    @Override
    public Year transformTo(AttributeValue input) {
        return Year.of(Integer.parseInt(input.n()));
    }

    @Override
    public EnhancedType<Year> type() {
        return EnhancedType.of(Year.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.N;
    }
}