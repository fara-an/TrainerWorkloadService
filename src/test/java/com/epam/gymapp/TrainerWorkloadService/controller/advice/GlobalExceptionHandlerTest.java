package com.epam.gymapp.TrainerWorkloadService.controller.advice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void entityNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Error occurred during database interaction."));

    }

    @Test
    void missingRequestParam_shouldReturn400() throws Exception {
        mockMvc.perform(get("/test/missing-param")) // missing param=...
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString("Request parameter param")));
    }

    @Test
    void missingPathVariable_shouldReturn400() throws Exception {
        mockMvc.perform(get("/test/missing-path"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString("Request parameter")));
    }

    @Test
    void methodArgumentNotValid_shouldReturn400() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/test/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("name must not be blank"));
    }


    @Test
    void genericException_shouldReturn500() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Something went wrong on our side. Please try again later."));
    }


}
