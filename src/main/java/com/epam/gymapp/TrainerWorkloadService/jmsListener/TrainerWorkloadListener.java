package com.epam.gymapp.TrainerWorkloadService.jmsListener;

import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
@Profile("!no-integrations")
@Component
public class TrainerWorkloadListener {



    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadListener.class);
    private final TrainerWorkloadService trainerWorkloadService;

    public TrainerWorkloadListener(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

//    @SqsListener("${cloud.aws.queue.name}")
//    public void onMessage(TrainerWorkloadRequest trainerWorkloadRequest) {
//        trainerWorkloadService.processTrainerWorkload(trainerWorkloadRequest);
//    }

    @SqsListener("${cloud.aws.queue.name}")
    public void onMessage(String message) {
        System.out.println(message);
    }

//    @JmsListener(destination = "ActiveMQ.DLQ", concurrency = "3-10")
//    public void onDeadLetter(Object failedMessage) {
//        LOGGER.error("Message moved to DLQ: {} ", failedMessage);
//    }
}
