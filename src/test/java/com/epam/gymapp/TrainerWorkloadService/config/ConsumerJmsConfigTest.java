package com.epam.gymapp.TrainerWorkloadService.config;

import com.epam.gymapp.TrainerWorkloadService.dto.TrainerWorkloadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ConsumerJmsConfig.class)
class ConsumerJmsConfigTest {

    @Autowired
    private ApplicationContext context;


    @Test
    void objectMapper_ShouldHaveJavaTimeModuleAndDisableTimestamps() {
        ObjectMapper mapper = context.getBean(ObjectMapper.class);

        assertNotNull(mapper);
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    @Test
    void messageConverter_ShouldHaveTypeIdMappings() throws NoSuchFieldException, IllegalAccessException {
        MappingJackson2MessageConverter converter =
                (MappingJackson2MessageConverter) context.getBean("jacksonJmsMessageConverter");

        assertNotNull(converter);

        var typedIdMappings = (Map<String, Class<?>>) getField(converter, "idClassMappings");
        assertEquals(TrainerWorkloadRequest.class, typedIdMappings.get("TrainerWorkloadRequest"));
        assertEquals(TrainerWorkloadRequest.class, typedIdMappings.get("epam.lab.gymapp.dto.request.trainerWorkloadRequest.TrainerWorkloadRequest"));

        String typeIdPropertyName = (String) getField(converter, "typeIdPropertyName");
        assertEquals("_type", typeIdPropertyName);

        ObjectMapper configured = (ObjectMapper) getField(converter, "objectMapper");
        ObjectMapper bean = context.getBean(ObjectMapper.class);
        assertSame(bean, configured);
    }

    @Test
    void jmsListenerContainerFactory_ShouldBeConfigured() throws IllegalAccessException, NoSuchFieldException {
        DefaultJmsListenerContainerFactory factory = context.getBean(DefaultJmsListenerContainerFactory.class);

        assertNotNull(factory);
        assertEquals("3-10",getFieldFromHierarchy(factory, "concurrency"));

    }

    @Test
    void activeMQConnectionFactory_ShouldHaveRedeliveryPolicyConfigured() {
        ActiveMQConnectionFactory factory = context.getBean(ActiveMQConnectionFactory.class);

        assertNotNull(factory);
        RedeliveryPolicy policy = factory.getRedeliveryPolicy();
        assertNotNull(policy);
        assertEquals(3, policy.getMaximumRedeliveries());
        assertEquals(2000, policy.getInitialRedeliveryDelay());
        assertTrue(policy.isUseExponentialBackOff());
        assertEquals(2.0, policy.getBackOffMultiplier());
    }

    private static Object getField(Object target, String name) throws NoSuchFieldException, IllegalAccessException {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(target);
    }

    private static Object getFieldFromHierarchy(Object target, String name) throws IllegalAccessException, NoSuchFieldException {
        Class<?> c = target.getClass();
        while (c != null) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                return f.get(target);
            } catch (NoSuchFieldException ignored) {
                c = c.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in class hirearchy of " + target.getClass());

    }
}
