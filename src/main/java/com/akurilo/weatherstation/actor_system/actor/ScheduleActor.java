package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.service.WeatherConditionServiceImpl;
import com.akurilo.weatherstation.service.WeatherConditionSevice;
import dto.StationDto;
import enums.RequestType;
import org.springframework.web.client.RestTemplate;
import scala.concurrent.duration.FiniteDuration;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static com.akurilo.weatherstation.Application.ACTOR_SYSTEM;
import static com.akurilo.weatherstation.Application.TIMEOUT_GET_MESSAGE;


public class ScheduleActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Cancellable cancellable;
    private WeatherConditionSevice weatherConditionSevice = new WeatherConditionServiceImpl(new RestTemplate());

    public static Props props() {
        return Props.create(ScheduleActor.class);
    }

    @Override
    public void preStart() {
        cancellable = context().system().scheduler().schedule(
                FiniteDuration.create(10, TimeUnit.SECONDS),
                FiniteDuration.create(5, TimeUnit.SECONDS),
                getSelf(),
                "Do Scheduled Work",
                context().dispatcher(),
                ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::getCurrentWeatherCondition)
                //TODO change period in schedule
                //TODO turn of schedule
                .build();
    }

    private void getCurrentWeatherCondition(String message) {
        try {
            if (!cancellable.isCancelled()) {
                StationDto stationDto = new StationDto();
                stationDto.setRequestType(RequestType.GET_LIST);
                final ActorRef childActor = ACTOR_SYSTEM.actorOf(StationServiceActor.props());
                CompletableFuture<Object> future = ask(childActor, stationDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();

                //TODO bussines logic weatherConditionSevice.getCurrentWeatherCondition()
                System.out.println("========== Schedule message =========  " + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
            }
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }
}

