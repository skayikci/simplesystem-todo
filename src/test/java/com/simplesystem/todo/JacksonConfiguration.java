package com.simplesystem.todo;

import java.time.LocalDateTime;

import com.simplesystem.todo.controller.adapter.LocalDateTimeDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJackson() {
        return builder -> builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
    }
}