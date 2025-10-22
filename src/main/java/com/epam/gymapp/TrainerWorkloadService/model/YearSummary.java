package com.epam.gymapp.TrainerWorkloadService.model;

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

    public int getTotalDuration(){
       return months.stream().mapToInt(MonthSummary::getTotalDuration).sum();
    }

}
