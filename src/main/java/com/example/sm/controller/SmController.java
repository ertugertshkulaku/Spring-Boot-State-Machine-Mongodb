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
import org.springframework.statemachine.config.model.StateMachineModel;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping
public class SmController {

    @Autowired
    private EntityContextRepository entityContextRepository;
    private final  StateMachineService<String, String> stateEntity1EventStateMachineService;
   // private final StateMachineFactory<String, String> stateMachineFactory;
//    private final DefaultStateMachineService<String, String> stateMachineService;
   private final StateMachineRepository<MongoDbRepositoryStateMachine> stateMachineRepository;
    private StateMachine<String, String> currentStateMachine;
    private final StateMachineFactory<String, String> stateMachineFactory;




    public SmController(
            StateMachineService<String, String> stateEntity1EventStateMachineService,
//            StateMachineFactory<String, String> stateMachineFactory
//            DefaultStateMachineService<String, String> stateMachineService,
            StateMachineRepository<MongoDbRepositoryStateMachine> stateMachineRepository,
            StateMachineFactory<String, String> stateMachineFactory
    ) {
        this.stateEntity1EventStateMachineService = stateEntity1EventStateMachineService;
        //this.stateMachineFactory = stateMachineFactory;
//        this.stateMachineService = stateMachineService;
        this.stateMachineRepository  =stateMachineRepository;
        this.stateMachineFactory = stateMachineFactory;
    }

    @PostMapping("/save")
    public void saveEntity(){
        entityContextRepository.save(new Entity());
    }



    @GetMapping(value = "/entity1/")
    public ResponseEntity<Void> changeEvent(@RequestParam String entity1Id, @RequestParam String event) throws Exception {
       // String machineId = String.valueOf(entity1Id);
        //machineId = "";
        /*StateMachine<String, String> sm = stateMachineFactory.getStateMachine();
        MongoDbRepositoryStateMachine machine = new MongoDbRepositoryStateMachine();
        machine.setMachineId(entity1Id);
        machine.setState(sm.getInitialState().getId());
        // raw byte[] representation of a context
        machine.setStateMachineContext(new byte[] { 0 });

        stateMachineRepository.save(machine);*/
//        MongoDbRepositoryStateMachine machine = new MongoDbRepositoryStateMachine();
//        machine.setMachineId(entity1Id);
//        stateMachineRepository.save(machine);
        //MongoDbRepositoryState
//        StateMachine<String, String> smf = stateMachineFactory.getStateMachine(entity1Id);

//        Optional<MongoDbRepositoryStateMachine> instance = stateMachineRepository.findById(entity1Id);


           // StateMachine<String, String> sm = stateMachineFactory.getStateMachine();

        //StateMachine<String, String> sm = stateMachineFactory.getStateMachine();
//        StateMachine<String, String> sm = stateMachineFactory.getStateMachine();
//        MongoDbRepositoryStateMachine machine = new MongoDbRepositoryStateMachine();
//        machine.setMachineId(entity1Id);
//        machine.setState(sm.getInitialState().getId());
//        // raw byte[] representation of a context
//        machine.setStateMachineContext(new byte[] { 0 });
//        stateMachineRepository.save(machine);

        StateMachine<String, String> sms = stateEntity1EventStateMachineService.acquireStateMachine(entity1Id);

            //stateMachineService.hasStateMachine(entity1Id);

            //StateMachine<String, String> sms = stateEntity1EventStateMachineService.acquireStateMachine(entity1Id);
        //StateMachine<String, String> sms = stateMachineFactory.getStateMachine();
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

   /* private synchronized StateMachine<String, String> getStateMachine(String machineId) {

        if (currentStateMachine == null) {
             currentStateMachine = stateEntity1EventStateMachineService.acquireStateMachine(machineId, false);
            currentStateMachine.startReactively().block();
        } else if (!ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId)) {
            stateEntity1EventStateMachineService.releaseStateMachine(currentStateMachine.getId());
            currentStateMachine.stopReactively().block();
            currentStateMachine = stateEntity1EventStateMachineService.acquireStateMachine(machineId, false);
            currentStateMachine.startReactively().block();
        }
        return currentStateMachine;
    }
*/

}
