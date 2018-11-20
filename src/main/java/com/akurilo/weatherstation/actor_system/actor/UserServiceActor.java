package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.entity.UserEntity;
import com.akurilo.weatherstation.service.UserServiceImpl;
import dto.UserDto;
import enums.UserRole;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceActor extends BaseServiceActor<UserServiceImpl, UserEntity> {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(UserServiceActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserDto.class, this::sendUserDto)
                .build();
    }

    private void sendUserDto(UserDto userDto) {
        try {
            //TODO add mapper to entity
            UserEntity userEntity = new UserEntity();
            userEntity.setId(userDto.getId());
            userEntity.setEmail(userDto.getEmail());
            userEntity.setRole(UserRole.fromString(userDto.getRole()));

            List<UserEntity> entities = actions(userEntity, userDto.getRequestType());

            //TODO add mapper from entity
            List<UserDto> userDtos = entities.stream().map(e -> {
                UserDto userDto1 = new UserDto();
                userDto1.setId(e.getId());
                userDto1.setEmail(e.getEmail());
                userDto1.setRole(e.getRole().getText());
                userDto1.setCreateOn(e.getCreateOn());
                userDto1.setLastUpdate(e.getLastUpdate());
                return userDto1;
            }).collect(Collectors.toList());

            getSender().tell(userDtos, ActorRef.noSender());
            log.info("Send message: {} to {}.class", userDto.toString(), MasterActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: {}", e.getMessage());
        }
    }
}
