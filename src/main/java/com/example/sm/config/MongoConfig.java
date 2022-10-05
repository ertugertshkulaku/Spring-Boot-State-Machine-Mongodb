package com.example.sm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;

import org.springframework.statemachine.persist.StateMachineRuntimePersister;


@Slf4j
@Configuration
public class MongoConfig {

   /* private final MongoDbStateMachineRepository mongodbStateMachineRepository;

    public MongoConfig(MongoDbStateMachineRepository mongodbStateMachineRepository) {
        this.mongodbStateMachineRepository = mongodbStateMachineRepository;
    }*/

    @Bean
    public StateMachineRuntimePersister<String, String, String> Entity1StateMachineRuntimePersister(
            EntityStateMachinePersist entityStateMachinePersist) {
        return new Entity1PersistingStateMachineInterceptor(entityStateMachinePersist);
    }


   /* @Bean
    public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersisted() {
        return new MongoDbPersistingStateMachineInterceptor<>(mongodbStateMachineRepository);
    }*/

 /*    @Bean
    public StateMachinePersister<String, String, String> persisted(
            StateMachinePersist<String, String, String> defaultPersist) {
        return new DefaultStateMachinePersister<>(defaultPersist);
    }*/

}
