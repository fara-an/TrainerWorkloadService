package com.epam.gymapp.TrainerWorkloadService.repository;

import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class TrainerWorkloadRepository {

    private final DynamoDbTable<TrainerWorkload> table;
    @Value("${dynamoDb.name}")
    private String dbName;


    public TrainerWorkloadRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table = dynamoDbEnhancedClient.table(dbName, TableSchema.fromBean(TrainerWorkload.class));
    }

    public void save(TrainerWorkload trainerWorkload) {
        table.putItem(trainerWorkload);
    }

    public Optional< TrainerWorkload >findById(String username) {
        return Optional.ofNullable(table.getItem(Key.builder().partitionValue(username).build()));
    }
}
