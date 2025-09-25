package com.epam.gymapp.TrainerWorkloadService.cucumber.component;

import com.epam.gymapp.TrainerWorkloadService.api.controller.TrainerWorkloadController;
import com.epam.gymapp.TrainerWorkloadService.controller.advice.GlobalExceptionHandler;
import com.epam.gymapp.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import com.epam.gymapp.TrainerWorkloadService.service.TrainerWorkloadService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
@WebMvcTest(controllers = TrainerWorkloadController.class)
@Import(GlobalExceptionHandler.class)
public class CucumberSpringComponentTestConfiguration {

    @MockitoBean
    private TrainerWorkloadService trainerWorkloadService;

    @MockitoBean
    private TrainerWorkloadRepository trainerWorkloadRepository;

}
