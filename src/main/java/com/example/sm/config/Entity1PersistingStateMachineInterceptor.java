package com.example.sm.config;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.persist.AbstractPersistingStateMachineInterceptor;

public class Entity1PersistingStateMachineInterceptor
        extends AbstractPersistingStateMachineInterceptor<String, String, String>
        implements StateMachineRuntimePersister<String, String, String> {

    private final EntityStateMachinePersist persist;

    public Entity1PersistingStateMachineInterceptor(EntityStateMachinePersist entityStateMachinePersist) {
        this.persist = entityStateMachinePersist;
    }

    @Override
    public void write(StateMachineContext<String, String> context, String contextObj) throws Exception {
        persist.write(context, contextObj);
    }

    @Override
    public StateMachineContext<String, String> read(String contextObj) throws Exception {
        return persist.read(contextObj);
    }

    @Override
    public StateMachineInterceptor<String, String> getInterceptor() {
        return this;
    }
}