package com.example.sm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.*;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.*;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryAction;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryGuard;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.statemachine.data.support.StateMachineJackson2RepositoryPopulatorFactoryBean;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import java.io.IOException;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class SmConfig extends StateMachineConfigurerAdapter<String, String> {




    private final  StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;
    private  final StateRepository<MongoDbRepositoryState> stateRepository;
    private  final  TransitionRepository<MongoDbRepositoryTransition> transitionRepository;
    private final   ActionRepository<MongoDbRepositoryAction> actionRepository;
    private final   GuardRepository<MongoDbRepositoryGuard> guardRepository;
    private  final  ResourceLoader resourceLoader;


    public SmConfig(
            StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister,
            StateRepository<MongoDbRepositoryState> stateRepository,
            TransitionRepository<MongoDbRepositoryTransition> transitionRepository,
            ActionRepository<MongoDbRepositoryAction> actionRepository,
            GuardRepository<MongoDbRepositoryGuard> guardRepository,
            ResourceLoader resourceLoader
    ) {
        this.stateMachineRuntimePersister = stateMachineRuntimePersister;
        this.stateRepository = stateRepository;
        this.transitionRepository = transitionRepository;
        this.actionRepository = actionRepository;
        this.guardRepository = guardRepository;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public StateMachineJackson2RepositoryPopulatorFactoryBean Jackson2RepositoryPopulateFactoryBean(){
        log.debug("BEGIN: [{}]");
        StateMachineJackson2RepositoryPopulatorFactoryBean factoryBean = new StateMachineJackson2RepositoryPopulatorFactoryBean();
        try {
            if (!checkIfExistStateTransitionGuardActionOnDb()) {
                Resource[] resources = ResourcePatternUtils
                        .getResourcePatternResolver(resourceLoader).getResources("classpath*:test_state.json");
                factoryBean.setResources(resources);
            }
        }catch (IOException e){
            log.error("error [{}]", e.getMessage());
        }
        log.debug("END: [{}]" );
        return factoryBean;
    }

    private boolean checkIfExistStateTransitionGuardActionOnDb(){
        log.debug("BEGIN: [{}]" );
        boolean exist = stateRepository.findAll().iterator().hasNext()  ||
                transitionRepository.findAll().iterator().hasNext() ||
                actionRepository.findAll().iterator().hasNext() ||
                guardRepository.findAll().iterator().hasNext();
        log.debug("END: [{}]");
        return exist;
    }



    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withPersistence()
                .runtimePersister(stateMachineRuntimePersister);
        config.withConfiguration().autoStartup(false);
    }




    @Override
    public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
        model.withModel()
                .factory(modelFactory());

    }

    @Bean
    public StateMachineModelFactory<String, String> modelFactory() {
        return new CustomRepositoryStateMachineModelFactory(stateRepository, transitionRepository);
    }

}


