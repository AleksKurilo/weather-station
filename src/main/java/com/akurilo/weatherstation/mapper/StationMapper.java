package com.akurilo.weatherstation.mapper;

import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.entity.StationEntity;
import com.google.gson.Gson;
import dto.CoordinateDto;
import dto.StationDto;
import org.springframework.stereotype.Component;

@Component
public class StationMapper extends BaseMapper<StationEntity, StationDto> {

    private Gson gson = new Gson();

    //TODO Create in database table for coordinate and remove Gson dependency in Gradle
    @Override
    public StationEntity toEntity(StationDto dto) {
        StationEntity entity = super.toEntity(dto);

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setId(dto.getLocationId());
        entity.setLocation(locationEntity);

        String coordinate = gson.toJson(dto.getCoordinate());
        entity.setCoordinate(coordinate);
        return entity;
    }

    @Override
    public StationDto fromEntity(StationEntity entity) {
        StationDto dto = super.fromEntity(entity);
        dto.setLocationId(entity.getLocation().getId());

        CoordinateDto coordinateDto = gson.fromJson(entity.getCoordinate(), CoordinateDto.class);
        dto.setCoordinate(coordinateDto);
        return dto;
    }
}
