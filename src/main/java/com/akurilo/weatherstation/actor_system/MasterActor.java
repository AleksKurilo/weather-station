package com.akurilo.weatherstation.actor_system;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.actor.CenterActor;
import com.akurilo.weatherstation.actor_system.actor.LocationActor;
import com.akurilo.weatherstation.actor_system.actor.StationServiceActor;
import com.akurilo.weatherstation.actor_system.actor.UserServiceActor;
import dto.*;

import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;
import static com.akurilo.weatherstation.Application.ACTOR_SYSTEM;
import static com.akurilo.weatherstation.Application.TIMEOUT_GET_MESSAGE;

public class MasterActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Cluster cluster = Cluster.get(getContext().system());

    public static Props props() {
        return Props.create(MasterActor.class);
    }

    @Override
    public void preStart() {
        cluster.subscribe(self(), ClusterEvent.MemberUp.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AuthRequestDto.class, this::getUserByEmail)
                .match(UserDto.class, this::sentUserDto)
                .match(LocationDto.class, this::sendLocationDto)
                .match(StationDto.class, this::sendStationDto)
                .match(CenterDto.class, this::sendCenterDto)
                .build();
    }

    private void getUserByEmail(AuthRequestDto authRequestDto) {
        try {
            final ActorRef childActor = ACTOR_SYSTEM.actorOf(UserServiceActor.props());
            CompletableFuture<Object> future = ask(childActor, authRequestDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
            pipe(future, getContext().dispatcher()).to(sender());
            log.info("Send message: {} to {}.class", authRequestDto.toString(), StationServiceActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }

    private void sentUserDto(UserDto userDto) {
        try {
            final ActorRef childActor = ACTOR_SYSTEM.actorOf(UserServiceActor.props());
            CompletableFuture<Object> future = ask(childActor, userDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
            pipe(future, getContext().dispatcher()).to(sender());
            log.info("Send message: {} to {}.class", userDto.toString(), UserServiceActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }

    private void sendStationDto(StationDto stationDto) {
        try {
            final ActorRef childActor = ACTOR_SYSTEM.actorOf(StationServiceActor.props());
            CompletableFuture<Object> future = ask(childActor, stationDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
            pipe(future, getContext().dispatcher()).to(sender());
            log.info("Send message: {} to {}.class", stationDto.toString(), StationServiceActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }

    private void sendLocationDto(LocationDto locationDto) {
        try {
            final ActorRef childActor = ACTOR_SYSTEM.actorOf(LocationActor.props());
            CompletableFuture<Object> future = ask(childActor, locationDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
            pipe(future, getContext().dispatcher()).to(sender());
            log.info("Send message: {} to {}.class", locationDto.toString(), UserServiceActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }

    private void sendCenterDto(CenterDto centerDto) {
        try {
            final ActorRef childActor = ACTOR_SYSTEM.actorOf(CenterActor.props());
            CompletableFuture<Object> future = ask(childActor, centerDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
            pipe(future, getContext().dispatcher()).to(sender());
            log.info("Send message: {} to {}.class", centerDto.toString(), UserServiceActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }
}
