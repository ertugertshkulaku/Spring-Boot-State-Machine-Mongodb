package com.example.sm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.config.model.*;
import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;

import java.util.ArrayList;
import java.util.Collection;

public class CustomStateMachineModelFactory implements StateMachineModelFactory<String, String> {

    private final StateRepository<? extends RepositoryState> stateRepository;
    private final TransitionRepository<? extends RepositoryTransition> transitionRepository;

    public CustomStateMachineModelFactory(StateRepository<MongoDbRepositoryState> stateRepository, TransitionRepository<MongoDbRepositoryTransition> transitionRepository) {
        this.stateRepository = stateRepository;
        this.transitionRepository = transitionRepository;
    }

    @Override
    public StateMachineModel<String, String> build() {
        Collection<StateData<String, String>> stateData = new ArrayList<>();
        stateRepository.findAll().forEach(s -> stateData.add(new StateData<>(s.getState(), s.isInitial())));

        StatesData<String, String> statesData = new StatesData<>(stateData);

        Collection<TransitionData<String, String>> transitionData = new ArrayList<>();
        transitionRepository.findAll().forEach( t -> transitionData.add(new TransitionData<>(t.getSource().getState(),
                t.getTarget() != null ? t.getTarget().getState() : null,
                t.getEvent())));

        TransitionsData<String, String> transitionsData = new TransitionsData<>(transitionData);
        return new DefaultStateMachineModel<>(null,
                statesData, transitionsData);
    }

    @Override
    public StateMachineModel<String, String> build(String s) {
        return build();
    }
}
