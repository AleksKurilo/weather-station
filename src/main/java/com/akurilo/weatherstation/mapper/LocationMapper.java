package com.akurilo.weatherstation.mapper;

import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.entity.StationEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.CoordinateDto;
import dto.LocationDto;
import dto.StationDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LocationMapper extends BaseMapper<LocationEntity, LocationDto> {

    private Gson gson = new Gson();

    //TODO Create in database table for coordinate and remove Gson dependency in Gradle
    @Override
    public LocationEntity toEntity(LocationDto dto) {
        LocationEntity entity = super.toEntity(dto);
        if (dto.getStations() != null) {
            Set<StationEntity> stationList = dto.getStations().stream()
                    .map(stationDto -> map(stationDto, StationEntity.class)
                    ).collect(Collectors.toSet());
            entity.setStations(stationList);
        }

        String coordinates = gson.toJson(dto.getCoordinates());
        entity.setCoordinates(coordinates);
        return entity;
    }

    @Override
    public LocationDto fromEntity(LocationEntity entity) {
        LocationDto dto = super.fromEntity(entity);
        if (entity.getStations() != null) {
            Set<StationDto> stations = entity.getStations().stream()
                    .map(stationEntity -> map(stationEntity, StationDto.class))
                    .collect(Collectors.toSet());
            dto.setStations(stations);
        }

        Type type = new TypeToken<Set<CoordinateDto>>() {
        }.getType();
        Set<CoordinateDto> coordinateDtos = gson.fromJson(entity.getCoordinates(), type);
        dto.setCoordinates(coordinateDtos);
        return dto;
    }
}
