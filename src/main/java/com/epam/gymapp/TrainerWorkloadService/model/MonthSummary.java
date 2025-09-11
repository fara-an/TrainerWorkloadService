package com.epam.gymapp.TrainerWorkloadService.model;

import com.epam.gymapp.TrainerWorkloadService.converter.MonthConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MonthSummary {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private YearSummary yearSummary;

    @Convert(converter = MonthConverter.class)
    @Column(name = "training_month")
    private Month month;

    @Column(name = "totalDuration")
    private Integer totalDuration;

    public MonthSummary(Month month, YearSummary yearSummary) {
        this.month = month;
        this.yearSummary = yearSummary;
        this.totalDuration = 0;
    }

    public void addDuration(int duration) {
        this.totalDuration += duration;
    }

    public void subtractDuration(int duration) {
        this.totalDuration -= duration;
    }


}
