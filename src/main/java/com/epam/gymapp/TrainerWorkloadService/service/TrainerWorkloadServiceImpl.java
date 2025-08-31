package com.epam.gymapp.TrainerWorkloadService.service;

import com.epam.gymapp.TrainerWorkloadService.dto.*;
import com.epam.gymapp.TrainerWorkloadService.exceptions.EntityNotFoundException;
import com.epam.gymapp.TrainerWorkloadService.model.MonthSummary;
import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import com.epam.gymapp.TrainerWorkloadService.model.YearSummary;
import com.epam.gymapp.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadServiceImpl.class);
    private final TrainerWorkloadRepository trainerWorkloadRepository;

    public TrainerWorkloadServiceImpl(TrainerWorkloadRepository trainerWorkloadRepository) {
        this.trainerWorkloadRepository = trainerWorkloadRepository;
    }

    @JmsListener(destination = "${queue.trainerWorkload}", concurrency = "3-10")
    @Transactional
    @Override
    public void processTrainerWorkload(TrainerWorkloadRequest workloadRequest) {
        LOGGER.debug("Started to process {}'s work hours", workloadRequest.getTrainerUsername());
        String trainerUsername = workloadRequest.getTrainerUsername();
        LocalDateTime date = workloadRequest.getTrainingDate();
        Year year = Year.from(date);
        LOGGER.debug("Year is {} ", year.getValue());
        Month month = date.getMonth();

//        if(true){
//            throw new RuntimeException("boom");
//        }

        TrainerWorkload trainerWorkload = trainerWorkloadRepository.findById(trainerUsername)
                .orElseGet(() -> {
                    TrainerWorkload newTrainerWorkload = new TrainerWorkload(trainerUsername);
                    newTrainerWorkload.setFirstName(workloadRequest.getFirstName());
                    newTrainerWorkload.setLastName(workloadRequest.getLastName());
                    newTrainerWorkload.setActive(workloadRequest.isActive());
                    return newTrainerWorkload;
                });

        YearSummary yearSummary = getOrCreateYear(trainerWorkload, year);
        MonthSummary monthSummary = getOrCreateMonth(yearSummary, month);


        if (ActionType.ADD == workloadRequest.getActionType()) {
            monthSummary.addDuration(workloadRequest.getDuration());
        } else if (ActionType.DELETE == workloadRequest.getActionType()) {
            monthSummary.subtractDuration(workloadRequest.getDuration());
        }
        trainerWorkloadRepository.save(trainerWorkload);
    }

    @JmsListener(destination = "ActiveMQ.DLQ", concurrency = "3-10")
    public void deadLetterHandler(Object failedMessage) {
        System.err.println("Message moved to DLQ: " + failedMessage);
    }

    @Transactional
    @Override
    public TrainerWorkloadSummaryResponse calculateTrainerWorkloadSummary(String trainerUsername) {
        TrainerWorkload trainerWorkload = trainerWorkloadRepository.findById(trainerUsername).orElseThrow(() -> new EntityNotFoundException("Trainer with username " + trainerUsername + " not found"));
        List<YearSummaryDto> years = trainerWorkload.getYears().stream().map(year -> new YearSummaryDto(
                year.getYear().getValue(),
                year.getMonths().stream()
                        .map(m -> new MonthSummaryDto(m.getMonth(), m.getTotalDuration()))
                        .toList())).toList();

        return new TrainerWorkloadSummaryResponse(
                trainerWorkload.getUsername(),
                trainerWorkload.getFirstName(),
                trainerWorkload.getLastName(),
                trainerWorkload.isActive(),
                years
        );
    }

    private YearSummary getOrCreateYear(TrainerWorkload trainerWorkload, Year year) {
        return trainerWorkload.getYears().stream()
                .filter(y -> y.getYear().equals(year))
                .findFirst()
                .orElseGet(() -> {
                    LOGGER.debug("New YearSummary is getting created ");
                    YearSummary yearSummary = new YearSummary(year, trainerWorkload);
                    trainerWorkload.addYearSummary(yearSummary);
                    return yearSummary;
                });
    }

    private MonthSummary getOrCreateMonth(YearSummary yearSummary, Month month) {
        return yearSummary.getMonths().stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseGet(() -> {
                    MonthSummary newMonth = new MonthSummary(month, yearSummary);
                    yearSummary.addMonthSummary(newMonth);
                    return newMonth;
                });
    }


}
