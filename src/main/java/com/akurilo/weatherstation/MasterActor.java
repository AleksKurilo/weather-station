package com.akurilo.weatherstation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class MasterActor extends AbstractActor {

    private final ActorRef slaveActor;

    public MasterActor(ActorRef slaveActor) {
        this.slaveActor = slaveActor;
    }

    public static Props props(ActorRef slaveActor) {
        return Props.create(MasterActor.class, () -> new MasterActor(slaveActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageDto.class, messageDto ->
                        slaveActor.tell(messageDto, getSelf())
                )
                .build();
    }
}
