package com.epam.gymapp.TrainerWorkloadService.repository;

import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadRepository extends JpaRepository< TrainerWorkload, String> {
}
