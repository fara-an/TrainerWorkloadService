package com.epam.gymapp.TrainerWorkloadService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class TrainerWorkload {

    @Id
    @Column(name = "username")
    private String username;

    @Setter
    @Column(name = "firstName")
    private String firstName;

    @Setter
    @Column(name = "lastname")
    private String lastName;

    @Setter
    @Column(name = "is_active")
    private boolean isActive;

    @Setter
    @OneToMany(mappedBy = "trainerWorkload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YearSummary> years = new ArrayList<>();

    public TrainerWorkload(String trainerUsername) {
        this.username = trainerUsername;
    }


    public void addYearSummary(YearSummary yearSummary) {
        years.add(yearSummary);
        yearSummary.setTrainerWorkload(this);
    }

}
