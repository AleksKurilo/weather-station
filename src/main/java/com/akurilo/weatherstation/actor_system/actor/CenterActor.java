package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.mapper.CenterMapper;
import com.akurilo.weatherstation.service.CenterService;
import dto.CenterDto;

import java.util.List;
import java.util.stream.Collectors;

public class CenterActor extends BaseServiceActor<CenterService, CenterEntity> {

    private final CenterMapper mapper = new CenterMapper();
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(CenterActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CenterDto.class, this::sendCenterDto)
                .build();
    }

    private void sendCenterDto(CenterDto centerDto) {
        try {
            CenterEntity centerEntity = mapper.toEntity(centerDto);
            List<CenterEntity> entities = executeRestRequest(centerEntity, centerDto.getRequestType());
            List<CenterDto> centerDtos = entities.stream()
                    .map(e -> mapper.fromEntity(e)).collect(Collectors.toList());

            getSender().tell(centerDtos, ActorRef.noSender());
            log.info("Send message: {} to {}.class", centerDtos.toString(), MasterActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: {}", e.getMessage());
        }
    }
}
