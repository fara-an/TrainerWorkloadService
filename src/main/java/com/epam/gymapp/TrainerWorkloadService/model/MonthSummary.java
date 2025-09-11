package com.epam.gymapp.TrainerWorkloadService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Month;

@Getter
@Setter
@NoArgsConstructor
public class MonthSummary {
    @Id
    private String id;

    private Month month;

    private Integer totalDuration;

    public MonthSummary(Month month, YearSummary yearSummary) {
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
