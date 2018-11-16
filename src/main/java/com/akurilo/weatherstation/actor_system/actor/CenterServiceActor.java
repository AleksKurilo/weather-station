package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.service.CenterServiceImpl;
import dto.CenterDto;

import java.util.ArrayList;
import java.util.List;


public class CenterServiceActor extends BaseServiceActor<CenterServiceImpl, CenterEntity> {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(CenterServiceActor.class);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(CenterDto.class, this::sendCenterDto)
                .build();
    }

    private void sendCenterDto(CenterDto centerDto) {
        try {

            //TODO add mapper to entity
            CenterEntity centerEntity = new CenterEntity();
            centerEntity.setId(centerDto.getId());
            centerEntity.setName(centerDto.getName());
            centerEntity.setCoordinates(centerDto.getCoordinates());

            List<CenterEntity> entities = actions(centerEntity, centerDto.getRequestType());

            //TODO mapper from entity
            List<CenterDto> centerDtos = new ArrayList<>();
            entities.stream().forEach(e -> {
                CenterDto centerDto1 = new CenterDto();
                centerDto1.setId(e.getId());
                centerDto1.setName(e.getName());
                centerDto1.setCoordinates(e.getCoordinates());
                centerDtos.add(centerDto1);
            });

            getSender().tell(centerDtos, ActorRef.noSender());
            log.info("Send message: {} to {}.class", centerDto.toString(), MasterActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: {}", e.getMessage());
        }
    }
}
