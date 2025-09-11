package com.epam.gymapp.TrainerWorkloadService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "trainer_workloads")
@NoArgsConstructor
@Getter
@Setter
public class TrainerWorkload {
    @Id
    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;

    private List<YearSummary> years = new ArrayList<>();

    public TrainerWorkload(String trainerUsername) {
        this.username = trainerUsername;
    }

    public void addYearSummary(YearSummary yearSummary) {
        years.add(yearSummary);
    }

}
