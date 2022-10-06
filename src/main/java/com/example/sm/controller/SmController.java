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

    public SmController(StateMachineService<String, String> stateEntity1EventStateMachineService) {
        this.stateEntity1EventStateMachineService = stateEntity1EventStateMachineService;
    }

    @PostMapping("/save")
    public void saveEntity(){
        entityContextRepository.save(new Entity());
    }



    @GetMapping(value = "/entity1/")
    public ResponseEntity<Void> changeEvent(@RequestParam String entity1Id, @RequestParam String event) {
        String machineId = String.valueOf(entity1Id);
        StateMachine<String, String> sm = stateEntity1EventStateMachineService.acquireStateMachine(machineId);
        log.info("entity1Id {}, event {}", entity1Id, event);
        log.info("event {}", sm.getState().getId());

        Message<String> message = MessageBuilder.withPayload(event).build();
        sm.sendEvent(Mono.just(message))
                .doOnComplete(() -> log.info("event sent {}", event)).subscribe();

//        if (sm.sendEvent(Entity1Event.valueOf(event))) {
//            log.info("event sent {}", Entity1Event.valueOf(event));
//            return entity1Repository.findById(entity1Id).map(ResponseEntity::ok).orElse(badRequest().build());
//        }
        return ResponseEntity.ok().build();
    }
}
