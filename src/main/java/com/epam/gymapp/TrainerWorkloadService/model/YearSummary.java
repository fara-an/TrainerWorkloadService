package com.epam.gymapp.TrainerWorkloadService.model;

import com.epam.gymapp.TrainerWorkloadService.converter.YearAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class YearSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrainerWorkload trainerWorkload;

    @Convert(converter = YearAttributeConverter.class)
    @Column(name = "training_year")
    private Year year;

    @OneToMany(mappedBy = "yearSummary", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MonthSummary> months = new ArrayList<>();

    public YearSummary( Year year, TrainerWorkload trainerWorkload) {
        this.trainerWorkload = trainerWorkload;
        this.year = year;
    }

    public MonthSummary getOrCreateMonth(Month month) {
       return months.stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseGet(()-> {
                    MonthSummary newMonth =  new MonthSummary(month, this);
                    this.addMonthSummary(newMonth);
                    return newMonth;
                });
    }

    public void addMonthSummary(MonthSummary monthSummary) {
        months.add(monthSummary);
        monthSummary.setYearSummary(this);
    }

    public void removeMonthSummary(MonthSummary monthSummary) {
        months.remove(monthSummary);
        monthSummary.setYearSummary(null);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof YearSummary)) return false;
        return id != null && id.equals(((YearSummary) obj).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
