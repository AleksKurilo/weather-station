package com.akurilo.weatherstation.actor_system;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.actor.CenterServiceActor;
import com.akurilo.weatherstation.dto.CenterDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static com.akurilo.weatherstation.Application.ACTOR_SYSTEM;
import static com.akurilo.weatherstation.Application.TIMEOUT_GET_MESSAGE;

public class MasterActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(MasterActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CenterDto.class, this::sendCenterDto)
                .build();
    }

    private void sendCenterDto(CenterDto centerDto) {
        try {
            final ActorRef childActor = ACTOR_SYSTEM.actorOf(CenterServiceActor.props());
            CompletableFuture<Object> future = ask(childActor, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
            List<CenterDto> answerDto = (List<CenterDto>) future.get();
            getSender().tell(answerDto, ActorRef.noSender());
            log.info("Send message: {} to {}.class", centerDto.toString(), CenterServiceActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }
}
