package com.epam.gymapp.TrainerWorkloadService.config;

import com.epam.gymapp.TrainerWorkloadService.converters.IntegerToYearConverter;
import com.epam.gymapp.TrainerWorkloadService.converters.YearToIntegerConverter;
import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfig {
    
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
            Arrays.asList(
                new IntegerToYearConverter(),
                new YearToIntegerConverter()
            )
        );
    }

    @Bean
    public CodecRegistry codecRegistry() {
        return fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
    }
}
