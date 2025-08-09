package com.epam.gymapp.TrainerWorkloadService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class TrainerWorkload {

    @Id
    @Column(name = "trainer_username")
    private String trainerUsername;

    @Setter
    @Column(name = "trainer_firstName")
    private String firstName;

    @Setter
    @Column(name = "trainer_lastname")
    private String lastName;

    @Setter
    @Column(name = "trainer_is_active")
    private boolean isActive;

    @Setter
    @OneToMany(mappedBy = "trainerWorkload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YearSummary> years = new ArrayList<>();

    public TrainerWorkload(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }


    public YearSummary getOrCreateYear(Year year) {
        return years.stream()
                .filter(y -> y.getYear().equals(year))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("New YearSummary is getting created ");
                    YearSummary yearSummary = new YearSummary(year, this);
                    this.addYearSummary(yearSummary);
                    return yearSummary;
                });
    }


    public void addYearSummary(YearSummary yearSummary) {
        System.out.println("Adding year summary to list");
        years.add(yearSummary);
        System.out.println(years.size());
        yearSummary.setTrainerWorkload(this);
    }

    public void removeYearSummary(YearSummary yearSummary) {
        years.remove(yearSummary);
        yearSummary.setTrainerWorkload(null);
    }
}
