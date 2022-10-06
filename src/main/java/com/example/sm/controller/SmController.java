package com.example.sm.controller;

import com.example.sm.model.Entity;
import com.example.sm.model.EntitySMContext;
import com.example.sm.repository.EntityContextRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping
public class SmController {

    @Autowired
    private EntityContextRepository entityContextRepository;

    private final  StateMachineService<String, String> stateEntity1EventStateMachineService;
    private final StateMachineFactory<String, String> stateMachineFactory;

    @Autowired
    StateMachineRepository<MongoDbRepositoryStateMachine> stateMachineRepository;


    public SmController(
            StateMachineService<String, String> stateEntity1EventStateMachineService,
            StateMachineFactory<String, String> stateMachineFactory
    ) {
        this.stateEntity1EventStateMachineService = stateEntity1EventStateMachineService;
        this.stateMachineFactory = stateMachineFactory;
    }

    @PostMapping("/save")
    public void saveEntity(){
        entityContextRepository.save(new Entity());
    }



    @GetMapping(value = "/entity1/")
    public ResponseEntity<Void> changeEvent(@RequestParam String entity1Id, @RequestParam String event) {
       // String machineId = String.valueOf(entity1Id);
        //machineId = "";
        /*StateMachine<String, String> sm = stateMachineFactory.getStateMachine();
        MongoDbRepositoryStateMachine machine = new MongoDbRepositoryStateMachine();
        machine.setMachineId(entity1Id);
        machine.setState(sm.getInitialState().getId());
        // raw byte[] representation of a context
        machine.setStateMachineContext(new byte[] { 0 });

        stateMachineRepository.save(machine);*/

        //MongoDbRepositoryState
        StateMachine<String, String> sms = stateEntity1EventStateMachineService.acquireStateMachine("");
        log.info("entity1Id {}, event {}", entity1Id, event);
        log.info("event {}", sms.getId());

        Message<String> message = MessageBuilder.withPayload(event).build();
        sms.sendEvent(Mono.just(message))
                .doOnComplete(() -> log.info("event sent {}", event)).subscribe();

//        if (sm.sendEvent(Entity1Event.valueOf(event))) {
//            log.info("event sent {}", Entity1Event.valueOf(event));
//            return entity1Repository.findById(entity1Id).map(ResponseEntity::ok).orElse(badRequest().build());
//        }
        return ResponseEntity.ok().build();
    }
}
