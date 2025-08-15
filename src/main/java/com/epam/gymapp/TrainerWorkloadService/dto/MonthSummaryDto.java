package com.epam.gymapp.TrainerWorkloadService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MonthSummaryDto {
    private Month month;
    private int totalDuration;
}
