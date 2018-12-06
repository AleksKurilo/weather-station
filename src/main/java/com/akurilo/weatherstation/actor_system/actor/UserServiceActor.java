package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.entity.UserEntity;
import com.akurilo.weatherstation.mapper.UserMapper;
import com.akurilo.weatherstation.service.UserService;
import dto.AuthRequestDto;
import dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceActor extends BaseServiceActor<UserService, UserEntity> {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final UserMapper mapper = new UserMapper();

    public static Props props() {
        return Props.create(UserServiceActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AuthRequestDto.class, this::getUserByEmail)
                .match(UserDto.class, this::sendUserDto)
                .build();
    }

    private void getUserByEmail(AuthRequestDto authRequestDto) {
        UserService userService = getService();
        Optional<UserEntity> userEntity = userService.getByEmail(authRequestDto.getEmail());

        userEntity.ifPresent(entity -> {
            UserDto userDto = mapper.fromEntity(entity);
            getSender().tell(userDto, ActorRef.noSender());
        });
    }

    private void sendUserDto(UserDto userDto) {
        try {
            UserEntity userEntity = mapper.toEntity(userDto);
            List<UserEntity> entities = actions(userEntity, userDto.getRequestType());
            List<UserDto> userDtos = entities.stream()
                    .map(e -> {
                        UserDto dto = mapper.fromEntity(e);
                        dto.setPassword("");
                        return dto;
                    })
                    .collect(Collectors.toList());

            getSender().tell(userDtos, ActorRef.noSender());
            log.info("Send message: {} to {}.class", userDto.toString(), MasterActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: {}", e.getMessage());
        }
    }
}
