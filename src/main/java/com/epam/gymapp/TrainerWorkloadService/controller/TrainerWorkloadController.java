package com.epam.gymapp.TrainerWorkloadService.controller;

import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadSummaryResponse;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer-workload")
public class TrainerWorkloadController {

    TrainerWorkloadService trainerWorkloadService;

    public TrainerWorkloadController(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @PostMapping
    public ResponseEntity<Void> handleWorkload(@RequestBody TrainerWorkloadRequest request) {
        trainerWorkloadService.processTrainerWorkload(request);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/trainer/{trainerUsername}")
    public ResponseEntity<TrainerWorkloadSummaryResponse> returnWorkload(@PathVariable("trainerUsername") String trainerUsername) {
        TrainerWorkloadSummaryResponse trainerWorkloadSummaryResponse = trainerWorkloadService.calculateTrainerWorkloadSummary(trainerUsername);
        return ResponseEntity.ok(trainerWorkloadSummaryResponse);
    }

}
