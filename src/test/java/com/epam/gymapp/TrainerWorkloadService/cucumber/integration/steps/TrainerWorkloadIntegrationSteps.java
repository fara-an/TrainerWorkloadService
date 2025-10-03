package com.epam.gymapp.TrainerWorkloadService.cucumber.integration.steps;

import com.epam.gymapp.TrainerWorkloadService.cucumber.integration.steps.dtos.Credentials;
import com.epam.gymapp.TrainerWorkloadService.cucumber.integration.steps.dtos.LoginResponse;
import com.epam.gymapp.TrainerWorkloadService.cucumber.integration.steps.dtos.TrainingAddDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class TrainerWorkloadIntegrationSteps {

    private TestRestTemplate restTemplate;

    private LocalDateTime startTime;
    private ObjectMapper objectMapper;
    private ResponseEntity<String> lastResponse;
    private HttpHeaders httpHeaders;
    private HttpEntity<?> httpEntity;

    String jwtToken;
    String baseGymAppUrl = "http://localhost:8082/gymapp";
    String trainerWorkloadUrl = "http://localhost:8081/trainer-workload-service/";


    @Before
    public void setUpUrl() throws JsonProcessingException {

        restTemplate = new TestRestTemplate();
        objectMapper = new ObjectMapper();
        httpHeaders = new HttpHeaders();
        Credentials credentials = new Credentials("Emily.Brown", "pass789");

        ResponseEntity<String> response = restTemplate.postForEntity(baseGymAppUrl + "/users/login", credentials, String.class);
        String responseBody = response.getBody();
        LoginResponse loginResponse = objectMapper.readValue(responseBody, LoginResponse.class);
        jwtToken = loginResponse.getToken();
        httpHeaders.setBearerAuth(jwtToken);
    }

    @When("I add a training for trainer {string} with spec {string} and trainee {string} with duration {int} minutes")
    public void iAddTraining(String trainerUsername, String spec, String traineeUsername, int duration) {
        this.startTime = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);
        TrainingAddDto newTraining = TrainingAddDto.builder()
                .trainingName("Some random")
                .trainerUserName(trainerUsername)
                .traineeUserName(traineeUsername)
                .trainingType(spec)
                .trainingDateStart(startTime)
                .duration(duration)
                .build();

        HttpEntity<?> httpEntity = new HttpEntity<>(newTraining, httpHeaders);
        lastResponse = restTemplate.postForEntity(
                baseGymAppUrl + "/trainings",
                httpEntity,
                String.class
        );
        assertThat(lastResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @When("I delete that training for trainer {string} with a trainee {string}")
    public void iDeleteTraining(String trainerUsername, String traineeUsername) {
        String url = baseGymAppUrl +
                "/trainings?trainerUsername=" + trainerUsername
                + "&traineeUsername=" + traineeUsername + "&startTime=" + startTime.toString();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

       restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Void.class);
    }


    @When("I request workload summary for {string}")
    public void iRequestWorkloadSummary(String username) {
        lastResponse = restTemplate.getForEntity(
                trainerWorkloadUrl
                        + "/trainer-workloads/" + username,
                String.class
        );
    }

    @When("I wait until the workload service processes the message")
    public void iWaitUntilWorkloadServiceProcesses() throws InterruptedException {
        Thread.sleep(2000);
    }


    @Then("the trainer workload summary for {string} should return 200 code")
    public void trainerWorkloadShouldIncludeMinutes(String username) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                trainerWorkloadUrl + "/trainer-workloads/" + username,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Then("I should receive a 404 Not Found error")
    public void iShouldReceive404NotFound() {
        assertThat(lastResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
