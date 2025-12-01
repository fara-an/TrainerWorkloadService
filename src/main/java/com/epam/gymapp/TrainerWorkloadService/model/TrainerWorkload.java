package com.epam.gymapp.TrainerWorkloadService.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainerWorkload {
    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;

    private List<YearSummary> years = new ArrayList<>();

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

    public TrainerWorkload(String trainerUsername) {
        this.username = trainerUsername;
    }

    public void addYearSummary(YearSummary yearSummary) {
        years.add(yearSummary);
    }

    public int getTotalDuration() {
        return years.stream().mapToInt(YearSummary::getTotalDuration).sum();
    }

}
