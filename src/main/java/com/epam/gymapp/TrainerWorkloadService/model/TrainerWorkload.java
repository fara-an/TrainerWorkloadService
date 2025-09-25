package com.epam.gymapp.TrainerWorkloadService.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "trainer_workloads")
@NoArgsConstructor
@AllArgsConstructor
@Data
@CompoundIndex(name = "first_last_idx", def = "{'firstName': 1, 'lastName': 1}")
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

    public int getTotalDuration() {
        return years.stream().mapToInt(YearSummary::getTotalDuration).sum();
    }

}
