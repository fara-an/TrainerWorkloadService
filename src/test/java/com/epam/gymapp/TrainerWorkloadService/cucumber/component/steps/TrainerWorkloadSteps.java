package com.epam.gymapp.TrainerWorkloadService.cucumber.component.steps;

import com.epam.gymapp.TrainerWorkloadService.dto.MonthSummaryDto;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadSummaryResponse;
import com.epam.gymapp.TrainerWorkloadService.dto.YearSummaryDto;
import com.epam.gymapp.TrainerWorkloadService.exceptions.EntityNotFoundException;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerWorkloadSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainerWorkloadService trainerWorkloadService; // This is a @MockBean from CucumberSpringConfig

    private ResultActions response;

    @Given("^trainer workload exists for username \"(.*)\"$")
    public void trainer_workload_exists(String username) {
        // build a sample response DTO
        YearSummaryDto y = new YearSummaryDto(2025,
                List.of(new MonthSummaryDto(Month.JANUARY, 120), new MonthSummaryDto(Month.FEBRUARY, 60)));
        TrainerWorkloadSummaryResponse dto = new TrainerWorkloadSummaryResponse(
                username, "John", "Doe", true, List.of(y)
        );
        reset(trainerWorkloadService);
        when(trainerWorkloadService.calculateTrainerWorkloadSummary(username)).thenReturn(dto);
    }

    @Given("^trainer workload does not exist for username \"(.*)\"$")
    public void trainer_workload_not_exists(String username) {
        reset(trainerWorkloadService);
        when(trainerWorkloadService.calculateTrainerWorkloadSummary(anyString()))
                .thenThrow(new EntityNotFoundException("not found"));
    }

    @When("^client requests trainer workload for username \"(.*)\"$")
    public void client_requests(String username) throws Exception {
        response = mockMvc.perform(get("/trainer-workloads/{trainerUsername}", username)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Then("^the response status should be (\\d+)$")
    public void assert_status(int statusCode) throws Exception {
        response.andExpect(status().is(statusCode));
    }

    @Then("^the response should contain trainerUsername \"(.*)\" and firstName \"(.*)\" and lastName \"(.*)\" and status (true|false)$")
    public void assert_body(String username, String firstName, String lastName, boolean status) throws Exception {
        response.andExpect(jsonPath("$.trainerUsername", is(username)))
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)))
                .andExpect(jsonPath("$.status", is(status)))
                .andExpect(jsonPath("$.years", hasSize(1)))
                .andExpect(jsonPath("$.years[0].year", is(2025)))
                .andExpect(jsonPath("$.years[0].months", hasSize(2)))
                .andExpect(jsonPath("$.years[0].months[0].month", is("JANUARY")))
                .andExpect(jsonPath("$.years[0].months[0].totalDuration", is(120)));
    }

    @Then("^the error message should be \"(.*)\"$")
    public void assert_error_message(String message) throws Exception {
        response.andExpect(jsonPath("$.message", is(message)));
    }
}