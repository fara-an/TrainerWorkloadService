package com.epam.gymapp.TrainerWorkloadService.cucumber.integration.steps;

import com.epam.gymapp.TrainerWorkloadService.dto.ActionType;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import com.epam.gymapp.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrainerWorkloadIntegrationSteps {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private TrainerWorkloadRepository repository;

    @Value("${queue.trainerWorkload}")
    private String queueName;

    private TrainerWorkloadRequest message;

    @Given("a trainer workload ADD message for trainer {string}")
    public void a_trainer_workload_add_message_for_trainer(String username) {
        message = new TrainerWorkloadRequest(
                username, "First", "Last",
                true, LocalDateTime.now(),
                60, ActionType.ADD
        );
    }

    @Given("an invalid trainer workload message with action {string} for trainer {string}")
    public void invalid_message(String action, String username) {
        message = new TrainerWorkloadRequest(
                username, "First", "Last",
                true, LocalDateTime.now(),
                60, ActionType.valueOf(action)
        );
    }

    @When("the message is sent to the workload queue")
    public void send_message() {
        jmsTemplate.convertAndSend(queueName, message);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    @When("a trainer workload DELETE message for trainer {string} is sent")
    public void send_delete_message(String username) {
        TrainerWorkloadRequest deleteMessage = new TrainerWorkloadRequest(
                username, "First", "Last",
                true, LocalDateTime.now(),
                60, ActionType.DELETE
        );
        jmsTemplate.convertAndSend(queueName, deleteMessage);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    @Then("the workload for {string} should exist in the database")
    public void workload_should_exist(String username) {
        Optional<TrainerWorkload> workload = repository.findById(username);
        assertThat(workload).isPresent();
    }

    @Then("the workload for {string} should be removed from the database")
    public void workload_should_be_removed(String username) {
        Optional<TrainerWorkload> workload = repository.findById(username);
        assertTrue(workload.isPresent());
        assertEquals(0,workload.get().getTotalDuration());
    }

    @Then("the workload for {string} should not exist in the database")
    public void workload_should_not_exist(String username) {
        Optional<TrainerWorkload> workload = repository.findById(username);
        assertThat(workload).isEmpty();
    }
}
