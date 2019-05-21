package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.service.WeatherConditionServiceImpl;
import com.akurilo.weatherstation.service.WeatherConditionSevice;
import dto.ScheduleManagementDto;
import dto.StationDto;
import enums.RequestType;
import org.springframework.web.client.RestTemplate;
import scala.concurrent.duration.FiniteDuration;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        cancellable = context()
                .system().scheduler()
                .schedule(
                        FiniteDuration.create(10, TimeUnit.SECONDS),
                        FiniteDuration.create(20, TimeUnit.SECONDS),
                        getSelf(),
                        new ScheduleManagementDto(false),
                        context().dispatcher(),
                        ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ScheduleManagementDto.class, this::getCurrentWeatherCondition)
                //TODO change period in schedule
                .build();
    }

    private void getCurrentWeatherCondition(ScheduleManagementDto scheduleManagementDto) {
        if (scheduleManagementDto.isCancel()) {
            cancellable.cancel();
        }
        try {
            if (!cancellable.isCancelled()) {
                log.info("=========> Send message to OPEN  WEATHER MAP " + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

                StationDto stationDto = new StationDto();
                stationDto.setRequestType(RequestType.GET_LIST);
                final ActorRef childActor = ACTOR_SYSTEM.actorOf(StationServiceActor.props());
                CompletableFuture<Object> future = ask(childActor, stationDto, TIMEOUT_GET_MESSAGE).toCompletableFuture();
                List<StationDto> stations = (List<StationDto>) future.get();
                //TODO Send list station in one actor message
                for (StationDto station : stations) {
                    station = weatherConditionSevice.getCurrentWeatherCondition(station);
                    station.setRequestType(RequestType.PUT);
                    childActor.tell(station, ActorRef.noSender());
                }
            }
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: ", e);
        }
    }
}

