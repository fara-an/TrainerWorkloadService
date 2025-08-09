package com.epam.gymapp.TrainerWorkloadService.service;

import com.epam.gymapp.TrainerWorkloadService.dto.MonthSummaryDto;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadSummaryResponse;
import com.epam.gymapp.TrainerWorkloadService.dto.YearSummaryDto;
import com.epam.gymapp.TrainerWorkloadService.exceptions.EntityNotFoundException;
import com.epam.gymapp.TrainerWorkloadService.model.MonthSummary;
import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import com.epam.gymapp.TrainerWorkloadService.model.YearSummary;
import com.epam.gymapp.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
public class TrainerWorkloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadService.class);
    public static final String ADD = "ADD";
    public static final String DELETE = "DELETE";


    private final TrainerWorkloadRepository trainerWorkloadRepository;

    public TrainerWorkloadService(TrainerWorkloadRepository trainerWorkloadRepository) {
        this.trainerWorkloadRepository = trainerWorkloadRepository;
    }

    public void processTrainerWorkload(TrainerWorkloadRequest workloadRequest) {
        LOGGER.debug("Started to process {}'s work hours", workloadRequest.getTrainerUsername());
        String trainerUsername = workloadRequest.getTrainerUsername();
        LocalDateTime date = workloadRequest.getTrainingDate();
        Year year = Year.from(date);
        LOGGER.debug("Year is {} ", year.getValue());
        Month month = date.getMonth();


        TrainerWorkload trainerWorkload = trainerWorkloadRepository.findById(trainerUsername)
                .orElseGet(() -> {
                    TrainerWorkload newTrainerWorkload = new TrainerWorkload(trainerUsername);
                    newTrainerWorkload.setFirstName(workloadRequest.getFirstName());
                    newTrainerWorkload.setLastName(workloadRequest.getLastName());
                    newTrainerWorkload.setActive(workloadRequest.isActive());
                    return newTrainerWorkload;
                });

        YearSummary yearSummary = trainerWorkload.getOrCreateYear(year);
        MonthSummary monthSummary = yearSummary.getOrCreateMonth(month);


        if (ADD.equals(workloadRequest.getActionType())) {
            monthSummary.addDuration(workloadRequest.getDuration());
        } else if (DELETE.equals(workloadRequest.getActionType())) {
            monthSummary.subtractDuration(workloadRequest.getDuration());
        }
        trainerWorkloadRepository.save(trainerWorkload);
    }

    public TrainerWorkloadSummaryResponse calculateTrainerWorkloadSummary(String trainerUsername) {
        TrainerWorkload trainerWorkload = trainerWorkloadRepository.findById(trainerUsername).orElseThrow(() -> new EntityNotFoundException("Trainer with username " + trainerUsername + " not foound"));
        List<YearSummaryDto> years = trainerWorkload.getYears().stream().map(year -> new YearSummaryDto(
                year.getYear().getValue(),
                year.getMonths().stream()
                        .map(m -> new MonthSummaryDto(m.getMonth(), m.getTotalDuration()))
                        .toList())).toList();

        return new TrainerWorkloadSummaryResponse(
                trainerWorkload.getTrainerUsername(),
                trainerWorkload.getFirstName(),
                trainerWorkload.getLastName(),
                trainerWorkload.isActive(),
                years


        );
    }


}
