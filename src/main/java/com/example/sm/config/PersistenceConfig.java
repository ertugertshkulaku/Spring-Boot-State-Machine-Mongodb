package com.example.sm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;


@Slf4j
@Configuration
public class PersistenceConfig {


/*
    @Bean
    public StateMachineRuntimePersister<String, String, String> Entity1StateMachineRuntimePersister(
            EntityStateMachinePersist persist) {
        return new EntityPersistingStateMachineInterceptor(persist);
    }
*/

    @Bean
    public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister(
            MongoDbStateMachineRepository jpaStateMachineRepository) {
        return new MongoDbPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineService<String, String> stateMachineService(
            StateMachineFactory<String, String> stateMachineFactory,
            StateMachineRuntimePersister<String, String, String> stateMachinePersist) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist);
    }
}
