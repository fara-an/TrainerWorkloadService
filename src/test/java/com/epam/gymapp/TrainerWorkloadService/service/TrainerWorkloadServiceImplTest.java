package com.epam.gymapp.TrainerWorkloadService.service;

import com.epam.gymapp.TrainerWorkloadService.dto.ActionType;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadSummaryResponse;
import com.epam.gymapp.TrainerWorkloadService.exceptions.EntityNotFoundException;
import com.epam.gymapp.TrainerWorkloadService.model.MonthSummary;
import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import com.epam.gymapp.TrainerWorkloadService.model.YearSummary;
import com.epam.gymapp.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TrainerWorkloadServiceImplTest {

    @Mock
    private TrainerWorkloadRepository trainerWorkloadRepository;
    private AutoCloseable autoCloseable;
    private TrainerWorkloadServiceImpl underTest;
    private ArgumentCaptor<TrainerWorkload> argumentCaptor = ArgumentCaptor.forClass(TrainerWorkload.class);

    @BeforeEach
    void initService() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new TrainerWorkloadServiceImpl(trainerWorkloadRepository);
    }

    @AfterEach
    void closeService() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void processTrainerWorkload_addNewTrainer_addsDuration() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();

        request.setTrainerUsername("john_doe");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setActive(true);
        request.setTrainingDate(LocalDateTime.of(2025, Month.MARCH, 10, 10, 0));
        request.setDuration(60);
        request.setActionType(ActionType.ADD);

        when(trainerWorkloadRepository.findById("john_doe")).thenReturn(Optional.empty());

        underTest.processTrainerWorkload(request);

        verify(trainerWorkloadRepository).save(argumentCaptor.capture());

        TrainerWorkload value = argumentCaptor.getValue();
        YearSummary yearSummary = value.getYears().getFirst();
        MonthSummary monthSummary = yearSummary.getMonths().getFirst();
        assertEquals(Year.of(2025), yearSummary.getYear());
        assertEquals(Month.MARCH, monthSummary.getMonth());
        assertEquals(60, monthSummary.getTotalDuration());
    }
    @Test
    void processTrainerWorkload_existingTrainer_deletesDuration() {
        TrainerWorkload existing = new TrainerWorkload("john_doe");
        YearSummary ys = new YearSummary(Year.of(2025), existing);
        MonthSummary ms = new MonthSummary(Month.MARCH, ys);
        ms.addDuration(120);
        ys.addMonthSummary(ms);
        existing.addYearSummary(ys);

        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setTrainerUsername("john_doe");
        request.setTrainingDate(LocalDateTime.of(2025, Month.MARCH, 10, 10, 0));
        request.setDuration(30);
        request.setActionType(ActionType.DELETE);

        when(trainerWorkloadRepository.findById("john_doe")).thenReturn(Optional.of(existing));

        underTest.processTrainerWorkload(request);

        assertEquals(90, ms.getTotalDuration());
        verify(trainerWorkloadRepository).save(existing);
    }

    @Test
    void calculateTrainerWorkloadSummary_existingTrainer_returnsSummary() {
        TrainerWorkload trainer = new TrainerWorkload("john_doe");
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setActive(true);

        YearSummary ys = new YearSummary(Year.of(2025), trainer);
        ys.addMonthSummary(new MonthSummary(Month.MARCH, ys));
        trainer.addYearSummary(ys);

        when(trainerWorkloadRepository.findById("john_doe")).thenReturn(Optional.of(trainer));

        TrainerWorkloadSummaryResponse response = underTest.calculateTrainerWorkloadSummary("john_doe");

        assertEquals("john_doe", response.getTrainerUsername());
        assertEquals("John", response.getFirstName());
        assertTrue(response.isStatus());
        assertEquals(1, response.getYears().size());
    }

    @Test
    void calculateTrainerWorkloadSummary_trainerNotFound_throwsException() {
        when(trainerWorkloadRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> underTest.calculateTrainerWorkloadSummary("missing"));
    }

}
