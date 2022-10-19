package com.example.sm.controller;

import com.example.sm.model.Entity;
import com.example.sm.repository.EntityContextRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping
public class SmController {

    private final  EntityContextRepository entityContextRepository;
    private final  StateMachineService<String, String> stateEntity1EventStateMachineService;




    public SmController(
            StateMachineService<String, String> stateEntity1EventStateMachineService,
            EntityContextRepository entityContextRepository

    ) {
        this.stateEntity1EventStateMachineService = stateEntity1EventStateMachineService;
        this.entityContextRepository  =entityContextRepository;
    }

    @PostMapping("/save")
    public void saveEntity(){
        entityContextRepository.save(new Entity());
    }



    @GetMapping(value = "/entity")
    public ResponseEntity<Void> changeEvent(@RequestParam String entity1Id, @RequestParam String event) {
/*        StateMachine<String, String> sms = stateEntity1EventStateMachineService.acquireStateMachine(entity1Id);
            log.info("entity1Id {}, event {}", entity1Id, event);
            log.info("event {}", sms.getId());

            Message<String> message = MessageBuilder.withPayload(event).build();
            sms.sendEvent(Mono.just(message))
                    .doOnComplete(() -> log.info("event sent {}", event)).subscribe();*/
        Mono.just(stateEntity1EventStateMachineService.acquireStateMachine(entity1Id))
                .subscribeOn(Schedulers.boundedElastic())
                .map(stringStringStateMachine -> stringStringStateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).doOnComplete(() -> log.info("event sent {}", event))).subscribe();
        return ResponseEntity.ok().build();
    }
}
