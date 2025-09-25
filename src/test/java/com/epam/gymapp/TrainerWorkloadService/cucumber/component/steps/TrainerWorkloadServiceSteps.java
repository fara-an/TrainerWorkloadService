package com.epam.gymapp.TrainerWorkloadService.cucumber.component.steps;

import com.epam.gymapp.TrainerWorkloadService.dto.ActionType;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import com.epam.gymapp.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadServiceImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TrainerWorkloadServiceSteps {

    private final TrainerWorkloadRepository repo = mock(TrainerWorkloadRepository.class);
    private final TrainerWorkloadService trainerWorkloadService = new TrainerWorkloadServiceImpl(repo);

    private TrainerWorkloadRequest request;
    private Exception exception;

    @Given("a workload request for trainer {string} with action {string}")
    public void aWorkloadRequestForTrainerWithAction(String userName, String action) {
        request = new TrainerWorkloadRequest();
        request.setTrainerUsername(userName);
        request.setTrainingDate(LocalDateTime.of(2025, 1, 1, 10, 0));
        request.setDuration(90);

        if ("ADD".equalsIgnoreCase(action)) {
            request.setActionType(ActionType.ADD);
        } else if ("DELETE".equalsIgnoreCase(action)) {
            request.setActionType(ActionType.DELETE);
        } else {
            request.setActionType(null);
        }

    }

    @When("I process the workload request")
    public void iProcessTheWorkloadRequest() {
        try {
            trainerWorkloadService.processTrainerWorkload(request);
        } catch (Exception e) {
            this.exception=e;
        }
    }

    @Then("the trainer workload should be saved")
    public void theTrainerWorkloadShouldBeSaved() {
        verify(repo, times(1)).save(any(TrainerWorkload.class));
        assert exception == null;
    }

    @Then("an exception should be thrown and nothing saved")
    public void anExceptionShouldBeThrownAndNothingSaved() {
        assertThrows(ResponseStatusException.class, () -> {
            if (exception != null) throw exception;
        });
        verify(repo, never()).save(any());
    }

}
