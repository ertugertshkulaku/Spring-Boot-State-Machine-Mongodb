//package com.example.sm.config;
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Input;
//import com.esotericsoftware.kryo.io.Output;
//import com.example.sm.model.Entity;
//import com.example.sm.model.EntitySMContext;
//import com.example.sm.repository.EntityContextRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.statemachine.StateMachineContext;
//import org.springframework.statemachine.StateMachinePersist;
//import org.springframework.statemachine.kryo.MessageHeadersSerializer;
//import org.springframework.statemachine.kryo.StateMachineContextSerializer;
//import org.springframework.statemachine.kryo.UUIDSerializer;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.util.Base64;
//import java.util.Optional;
//import java.util.UUID;
//
//@Slf4j
//@Component
//public class EntityStateMachinePersist implements StateMachinePersist<String, String, String> {
//    private final EntityContextRepository repository;
//
//    public EntityStateMachinePersist(EntityContextRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public void write(StateMachineContext<String, String> context, String contextObj) {
//        log.info("start to write to database {} {} {}", context, serialize(context).length(), contextObj);
//
//        Optional<Entity> entity = repository.findById(contextObj);
//        Optional<EntitySMContext> machineContext = entity.map(Entity::getEntitySMContext);
//        if (machineContext.isPresent()) {
//            machineContext.ifPresent(ctx -> {
//                ctx.setState(context.getState());
//                ctx.setContext(serialize(context));
//                repository.save(entity.get());
//            });
//        } else {
//
//            entity.get().setEntitySMContext(EntitySMContext
//                    .builder()
//                    .entityId(context.getId())
//                    .context(serialize(context))
//                    .state(context.getState())
//                    .build());
//            repository.save(entity.get());
//        }
//    }
//
//    @Override
//    public StateMachineContext<String, String> read(String contextObj) {
//        log.info("start to read from database {}", contextObj);
//        return repository
//                .findById(contextObj).map(Entity::getEntitySMContext)
//                .map(context -> {
//            StateMachineContext<String, String> stateMachineContext = deserialize(context.getContext());
//            log.info("read the current state {} ", stateMachineContext.getId());
//            return stateMachineContext;
//        }).orElse(null);
//    }
//
//
//    private String serialize(StateMachineContext<String, String> context) {
//        return Optional.ofNullable(context).map(ctx -> {
//            Kryo kryo = kryoThreadLocal.get();
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Output output = new Output(outputStream);
//            kryo.writeObject(output, context);
//            byte[] bytes = output.toBytes();
//            output.flush();
//            output.close();
//            return Base64.getEncoder().encodeToString(bytes);
//        }).orElse(null);
//    }
//
//    @SuppressWarnings("unchecked")
//    private StateMachineContext<String, String> deserialize(String data) {
//        return Optional.ofNullable(data).map(str -> {
//            Kryo kryo = kryoThreadLocal.get();
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
//            Input input = new Input(inputStream);
//            return kryo.readObject(input, StateMachineContext.class);
//        }).orElse(null);
//    }
//
//    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
//        Kryo kryo = new Kryo();
//        kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer<String, String>());
//        kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
//        kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
//        return kryo;
//    });
//}
