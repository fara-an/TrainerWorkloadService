package com.epam.gymapp.TrainerWorkloadService.repository;

import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository< TrainerWorkload, String> {
}
