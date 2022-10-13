package com.example.sm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.statemachine.StateMachinePersist;


import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachinePersist;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.util.UUID;


@Slf4j
@Configuration
public class MongoConfig {

    private final MongoDbStateMachineRepository mongodbStateMachineRepository;

    public MongoConfig(MongoDbStateMachineRepository mongodbStateMachineRepository) {
        this.mongodbStateMachineRepository = mongodbStateMachineRepository;
    }

    @Bean
    public StateMachineRuntimePersister<String, String, String> Entity1StateMachineRuntimePersister(
            EntityStateMachinePersist persist) {
        return new Entity1PersistingStateMachineInterceptor(persist);
    }

 /*   @Bean
    public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersisted() {
        return new MongoDbPersistingStateMachineInterceptor<>(mongodbStateMachineRepository);
    }*/




//    @Bean
//    public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister(
//            JpaStateMachineRepository jpaStateMachineRepository) {
//        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
//    }
//@Bean
//public StateMachineRuntimePersister<String, String, String> mongoPersist(
//        MongoDbStateMachineRepository mongoRepository) {
//    return new MongoDbPersistingStateMachineInterceptor<>(mongoRepository);
//}

//@Bean
//    public StateMachinePersister<String, String, String> persisted(
//            StateMachinePersist<String, String, String> defaultPersist) {
//        return new DefaultStateMachinePersister<>(defaultPersist);
//    }
}
