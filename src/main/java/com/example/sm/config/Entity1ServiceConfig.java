package com.example.sm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public  class Entity1ServiceConfig {
    @Bean
    public StateMachineService<String, String> entity1StateMachineService(
            StateMachineFactory<String, String> stateMachineFactory,
            StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
    }
}