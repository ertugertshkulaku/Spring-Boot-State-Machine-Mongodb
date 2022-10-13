package com.example.sm.config;

import org.springframework.statemachine.config.model.StateMachineModel;
import org.springframework.statemachine.data.*;

public class EriCus extends RepositoryStateMachineModelFactory {

    public EriCus(StateRepository<? extends RepositoryState> stateRepository, TransitionRepository<? extends RepositoryTransition> transitionRepository) {
        super(stateRepository, transitionRepository);
    }

    @Override
    public StateMachineModel<String, String> build(String machineId) {
        return super.build("");
    }
}
