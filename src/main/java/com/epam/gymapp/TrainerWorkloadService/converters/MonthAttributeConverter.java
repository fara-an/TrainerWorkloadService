package com.epam.gymapp.TrainerWorkloadService.converters;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Month;

public class MonthAttributeConverter implements AttributeConverter<Month> {
    @Override
    public AttributeValue transformFrom(Month month) {
        return AttributeValue.builder().s(month.name()).build();
    }

    @Override
    public Month transformTo(AttributeValue attributeValue) {
        return Month.valueOf(attributeValue.s());
    }

    @Override
    public EnhancedType<Month> type() {
        return EnhancedType.of(Month.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
