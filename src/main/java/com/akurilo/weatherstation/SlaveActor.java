package com.akurilo.weatherstation;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SlaveActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(SlaveActor.class, () -> new SlaveActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageDto.class, message ->
                        log.info(message.getMessage())
                )
                .build();
    }
}
