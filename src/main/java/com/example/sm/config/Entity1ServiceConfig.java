package com.example.sm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.AbstractStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import java.util.UUID;

@Configuration
public  class Entity1ServiceConfig {


   /* @Bean
    public StateMachineService<String, String> entity1StateMachineService(
             StateMachineFactory<String, String> stateMachineFactory,
             StateMachinePersist<String, String, String> stateMachinePersist) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist);
    }*/

      /*  @Bean
        public StateMachineService<String, String> entity2StateMachineService(
                StateMachineFactory<String, String> stateMachineFactory,
                StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister) {
            return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
        }*/

      @Bean
    public StateMachineService<String, String> stateMachineService(
             StateMachineFactory<String, String> stateMachineFactory,
             StateMachineRuntimePersister<String, String, String> stateMachinePersist) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist);
    }
}