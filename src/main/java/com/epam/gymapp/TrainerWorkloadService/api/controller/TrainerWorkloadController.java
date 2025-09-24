package com.epam.gymapp.TrainerWorkloadService.api.controller;

import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadSummaryResponse;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer-workloads")
public class TrainerWorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;

    public TrainerWorkloadController(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }


    @GetMapping("/{trainerUsername}")
    public ResponseEntity<TrainerWorkloadSummaryResponse> returnWorkload(@PathVariable("trainerUsername") String trainerUsername) {
        TrainerWorkloadSummaryResponse trainerWorkloadSummaryResponse = trainerWorkloadService.calculateTrainerWorkloadSummary(trainerUsername);
        return ResponseEntity.ok(trainerWorkloadSummaryResponse);
    }

}
