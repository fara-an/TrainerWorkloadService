package com.epam.gymapp.TrainerWorkloadService.model;

import com.epam.gymapp.TrainerWorkloadService.converters.YearAttributeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@NoArgsConstructor
public class YearSummary {

    private int id;

    private Year year;

    private List<MonthSummary> months = new ArrayList<>();

    public YearSummary(Year year) {
        this.id = year.getValue();
        this.year = year;
    }

    @DynamoDbConvertedBy(YearAttributeConverter.class)
    public Year getYear() {
        return year;
    }

    public void addMonthSummary(MonthSummary monthSummary) {
        months.add(monthSummary);
    }

    public void removeMonthSummary(MonthSummary monthSummary) {
        months.remove(monthSummary);
    }

    public int getTotalDuration() {
        return months.stream().mapToInt(MonthSummary::getTotalDuration).sum();
    }

}
