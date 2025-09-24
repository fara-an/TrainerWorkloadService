package com.epam.gymapp.TrainerWorkloadService.config;

import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;
@Profile("!local")
@Configuration
@EnableJms
public class ConsumerJmsConfig {

    Logger logger = LoggerFactory.getLogger(ConsumerJmsConfig.class);

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        Map<String, Class<?>> idMappings = new HashMap<>();
        idMappings.put("TrainerWorkloadRequest", TrainerWorkloadRequest.class);
        idMappings.put("epam.lab.gymapp.dto.request.trainerWorkloadRequest.TrainerWorkloadRequest", TrainerWorkloadRequest.class);

        converter.setTypeIdMappings(idMappings);
        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,  MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrency("3-10");
        factory.setErrorHandler(t -> logger.error("This is an error handler configure for JmsListener  ", t));
        return factory;
    }



    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setMaximumRedeliveries(3);
        policy.setInitialRedeliveryDelay(2000);
        policy.setBackOffMultiplier(2);
        policy.setUseExponentialBackOff(true);
        factory.setRedeliveryPolicy(policy);
        return factory;
    }

}
