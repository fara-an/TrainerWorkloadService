package com.epam.gymapp.TrainerWorkloadService.model;

import com.epam.gymapp.TrainerWorkloadService.converter.YearAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class YearSummary {

    @Id
    private int id;

    private Year year;

    private List<MonthSummary> months = new ArrayList<>();

    public YearSummary(Year year) {
        this.id=year.getValue();
        this.year = year;
    }


    public void addMonthSummary(MonthSummary monthSummary) {
        months.add(monthSummary);
    }

    public void removeMonthSummary(MonthSummary monthSummary) {
        months.remove(monthSummary);
    }

}
