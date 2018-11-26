package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.mapper.LocationMapper;
import com.akurilo.weatherstation.service.LocationService;
import dto.LocationDto;

import java.util.List;
import java.util.stream.Collectors;

public class LocationActor extends BaseServiceActor<LocationService, LocationEntity>{

    private final LocationMapper mapper = new LocationMapper();
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(LocationActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(LocationDto.class, this::sendLocationDto)
                .build();
    }

    private void sendLocationDto(LocationDto locationDto){
        try {
            LocationEntity locationEntity = mapper.toEntity(locationDto);
            List<LocationEntity> entities = actions(locationEntity, locationDto.getRequestType());
            List<LocationDto> locationDtos =  entities.stream()
                    .map(e -> mapper.fromEntity(e)).collect(Collectors.toList());

            getSender().tell(locationDtos, ActorRef.noSender());
            log.info("Send message: {} to {}.class", locationDtos.toString(), MasterActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            log.error("Error message: {}", e.getMessage());
        }
    }
}
