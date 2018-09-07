package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.dto.CenterDto;
import com.akurilo.weatherstation.service.CenterServiceImpl;

import java.util.List;


public class CenterServiceActor extends BaseServiceActor<CenterServiceImpl, CenterDto> {

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
            List<CenterDto> message = actions(centerDto);
            getSender().tell(message, ActorRef.noSender());
            log.info("Send message: {} to {}.class", centerDto.toString(), MasterActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: {}", e.getMessage());
        }
    }
}
