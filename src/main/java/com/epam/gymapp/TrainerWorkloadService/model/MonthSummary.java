package com.epam.gymapp.TrainerWorkloadService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.Month;
@DynamoDbBean
@Getter
@Setter
@NoArgsConstructor
public class MonthSummary {

    private String id;

    private Month month;

    private Integer totalDuration;

    public MonthSummary(Month month) {
        this.id = month.name();
        this.month = month;
        this.totalDuration = 0;
    }

    public void addDuration(int duration) {
        this.totalDuration += duration;
    }

    public void subtractDuration(int duration) {
        this.totalDuration -= duration;
    }


}
