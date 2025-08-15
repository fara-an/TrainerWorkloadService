package com.epam.gymapp.TrainerWorkloadService.api.controller;

import com.epam.gymapp.TrainerWorkloadService.dto.*;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Month;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerWorkloadController.class)
public class TrainerWorkloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrainerWorkloadService trainerWorkloadService;

    @Test
    void handleWorkload_returnsOk() throws Exception {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setTrainerUsername("john_doe");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setActive(true);
        request.setTrainingDate(java.time.LocalDateTime.of(2025, 3, 10, 10, 0));
        request.setDuration(60);
        request.setActionType(ActionType.ADD);

        mockMvc.perform(post("/trainer-workloads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(trainerWorkloadService).processTrainerWorkload(any(TrainerWorkloadRequest.class));
    }

    @Test
    void returnWorkload_returnsSummary() throws Exception {
        TrainerWorkloadSummaryResponse response = new TrainerWorkloadSummaryResponse(
                "john_doe",
                "John",
                "Doe",
                true,
                List.of(new YearSummaryDto(2025,
                        List.of(new MonthSummaryDto(Month.MARCH, 120))))
        );

        Mockito.when(trainerWorkloadService.calculateTrainerWorkloadSummary(eq("john_doe")))
                .thenReturn(response);

        mockMvc.perform(get("/trainer-workloads/john_doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainerUsername").value("john_doe"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.years[0].year").value(2025))
                .andExpect(jsonPath("$.years[0].months[0].month").value("MARCH"))
                .andExpect(jsonPath("$.years[0].months[0].totalDuration").value(120));
    }

}
