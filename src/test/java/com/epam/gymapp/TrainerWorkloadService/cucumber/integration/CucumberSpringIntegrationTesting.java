package com.epam.gymapp.TrainerWorkloadService.cucumber.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@CucumberContextConfiguration
@ActiveProfiles("test")
public class CucumberSpringIntegrationTesting {

    private static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private static final GenericContainer<?> activeMq = new GenericContainer<>("apache/activemq-artemis:2.31.2")
            .withExposedPorts(61616, 8161)
            .withEnv("ARTEMIS_USER", "admin")
            .withEnv("ARTEMIS_PASSWORD", "admin")
            .withReuse(false);

    static {
        activeMq.start();
        mongo.start();
    }

    @DynamicPropertySource
    static void configurePropertiesActiveMQ(DynamicPropertyRegistry registry) {
        registry.add("spring.activemq.broker-url",
                () -> "tcp://" + activeMq.getHost() + ":" + activeMq.getMappedPort(61616));
        registry.add("spring.activemq.user", () -> "admin");
        registry.add("spring.activemq.password", () -> "admin");
    }

    @DynamicPropertySource
    static void configurePropertiesMongo(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }
}
