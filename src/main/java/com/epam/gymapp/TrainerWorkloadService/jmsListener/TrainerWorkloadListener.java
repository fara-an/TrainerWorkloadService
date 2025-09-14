package com.epam.gymapp.TrainerWorkloadService.jmsListener;

import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!local")
public class TrainerWorkloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadListener.class);
    private final TrainerWorkloadService trainerWorkloadService;

    public TrainerWorkloadListener(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @JmsListener(destination = "${queue.trainerWorkload}", concurrency = "3-10")
    public void onMessage(TrainerWorkloadRequest trainerWorkloadRequest) {
        trainerWorkloadService.processTrainerWorkload(trainerWorkloadRequest);
    }

    @JmsListener(destination = "ActiveMQ.DLQ", concurrency = "3-10")
    public void onDeadLetter(Object failedMessage) {
        LOGGER.error("Message moved to DLQ: {} ", failedMessage);
    }
}
