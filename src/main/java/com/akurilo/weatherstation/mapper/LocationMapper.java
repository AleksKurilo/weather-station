package com.akurilo.weatherstation.mapper;

import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.entity.StationEntity;
import dto.LocationDto;
import dto.StationDto;

import java.util.HashSet;

public class LocationMapper extends BaseMapper<LocationEntity, LocationDto> {

    @Override
    public LocationEntity toEntity(LocationDto dto) {
        LocationEntity entity = super.toEntity(dto);
        entity.setStations(new HashSet<>());
        if (dto.getStations() != null) {
            dto.getStations().stream()
                    .forEach(stationDto -> {
                        StationEntity stationEntity = new StationEntity();
                        stationEntity.setId(stationDto.getId());
                        entity.getStations().add(stationEntity);
                    });
        }
        return entity;
    }

    @Override
    public LocationDto fromEntity(LocationEntity entity) {
        LocationDto dto = super.fromEntity(entity);
        dto.setStations(new HashSet<>());
        if (entity.getStations() != null) {
            entity.getStations().stream()
                    .forEach(StationEntity -> {
                        StationDto stationDto = map(StationEntity, StationDto.class);
                        dto.getStations().add(stationDto);
                    });
        }
        return dto;
    }
}
