package com.example.sm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.*;
import org.springframework.statemachine.data.support.StateMachineJackson2RepositoryPopulatorFactoryBean;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class SmConfig extends StateMachineConfigurerAdapter<String, String> {



    @Autowired
    private StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;

    @Autowired
    private  StateRepository<? extends RepositoryState> stateRepository;

    @Autowired
    private  TransitionRepository<? extends RepositoryTransition> transitionRepository;

    @Autowired
    private  ActionRepository<? extends RepositoryAction> actionRepository;
    @Autowired
    private  GuardRepository<? extends RepositoryGuard> guardRepository;

    @Autowired
    private  ResourceLoader resourceLoader;


   /* @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withPersistence().runtimePersister(stateMachineRuntimePersister);
        config.withConfiguration().autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial("BACKLOG", developersWakeUpAction())
                .state("IN_PROGRESS", weNeedCoffeeAction())
                .state("TESTING", qaWakeUpAction())
                .state("DONE", goToSleepAction())
                .end("DONE");
    }

    private Action<String, String> developersWakeUpAction() {
        return stateContext -> log.warn("Wake up lazy!");
    }

    private Action<String, String> weNeedCoffeeAction() {
        return stateContext -> log.warn("No coffee!");
    }

    private Action<String, String> qaWakeUpAction() {
        return stateContext -> log.warn("Wake up the testing team, the sun is high!");
    }

    private Action<String, String> goToSleepAction() {
        return stateContext -> log.warn("All sleep! the client is satisfied.");
    }


    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                .source("BACKLOG")
                .target("IN_PROGRESS")
                .event("START_FEATURE")
                .and()
                // DEVELOPERS:
                .withExternal()
                .source("IN_PROGRESS")
                .target("TESTING")
                .event("FINISH_FEATURE")
                .guard(alreadyDeployedGuard())
                .and()
                // QA-TEAM:
                .withExternal()
                .source("TESTING")
                .target("DONE")
                .event("QA_CHECKED_UC")
                .and()
                .withExternal()
                .source("TESTING")
                .target("IN_PROGRESS")
                .event("QA_REJECTED_UC")
                .and()
                // ROCK-STAR:
                .withExternal()
                .source("BACKLOG")
                .target("TESTING")
                .event("ROCK_STAR_DOUBLE_TASK")
                .and()
                // DEVOPS:
                .withInternal()
                .source("IN_PROGRESS")
                .event("DEPLOY")
                .action(deployPreProd())
                .and()
                .withInternal()
                .source("BACKLOG")
                .event("DEPLOY")
                .action(deployPreProd());
    }


    private static Guard<String, String> alreadyDeployedGuard() {
        return context -> Optional.ofNullable(
                        context.getExtendedState().getVariables().get("deployed"))
                .map(v -> (boolean) v)
                .orElse(false);
    }

    private static Action<String, String> deployPreProd() {
        return stateContext -> {
            log.warn("DEPLOY: Rolling out to pre-production.");
            stateContext.getExtendedState().getVariables().put("deployed", true);
        };
    }*/

    @Bean
    public StateMachineJackson2RepositoryPopulatorFactoryBean Jackson2RepositoryPopulateFactoryBean(){
        log.debug("BEGIN: [{}]");
        StateMachineJackson2RepositoryPopulatorFactoryBean factoryBean = new StateMachineJackson2RepositoryPopulatorFactoryBean();
        try {
            if (!checkIfExistStateTransitionGuardActionOnDb()) {
                Resource[] resources = ResourcePatternUtils
                        .getResourcePatternResolver(resourceLoader).getResources("classpath*:data.json");
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
        return new RepositoryStateMachineModelFactory(stateRepository, transitionRepository);
    }
}
