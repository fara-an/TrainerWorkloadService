package com.epam.gymapp.TrainerWorkloadService.service;

import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadSummaryResponse;

public interface TrainerWorkloadService {
    void processTrainerWorkload(TrainerWorkloadRequest workloadRequest);

    TrainerWorkloadSummaryResponse calculateTrainerWorkloadSummary(String trainerUsername);
}
