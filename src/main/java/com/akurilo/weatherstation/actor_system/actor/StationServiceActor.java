package com.akurilo.weatherstation.actor_system.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akurilo.weatherstation.actor_system.MasterActor;
import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.mapper.StationMapper;
import com.akurilo.weatherstation.service.StationService;
import dto.StationDto;

import java.util.List;
import java.util.stream.Collectors;


public class StationServiceActor extends BaseServiceActor<StationService, StationEntity> {

    private final StationMapper mapper = new StationMapper();
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(StationServiceActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StationDto.class, this::sendStationDto)
                .build();
    }

    private void sendStationDto(StationDto stationDto) {
        try {
            StationEntity stationEntity = mapper.toEntity(stationDto);
            List<StationEntity> entities = actions(stationEntity, stationDto.getRequestType());
            List<StationDto> stationDtos = entities.stream()
                    .map(mapper::fromEntity).collect(Collectors.toList());

            getSender().tell(stationDtos, ActorRef.noSender());
            log.info("Send message: {} to {}.class", stationDto.toString(), MasterActor.class.getSimpleName());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), ActorRef.noSender());
            log.error("Error message: {}", e.getMessage());
        }
    }
}
