package com.akurilo.weatherstation.mapper;

import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.entity.LocationEntity;
import dto.CenterDto;
import dto.LocationDto;

import java.util.HashSet;

public class CenterMapper extends BaseMapper<CenterEntity, CenterDto> {

    @Override
    public CenterEntity toEntity(CenterDto dto) {
        CenterEntity entity = super.toEntity(dto);
        entity.setLocations(new HashSet<>());
        if (dto.getLocations() != null) {
            dto.getLocations().stream()
                    .forEach(locationDto -> {
                        LocationEntity locationEntity = map(locationDto, LocationEntity.class);
                        locationEntity.setId(locationDto.getId());
                        entity.getLocations().add(locationEntity);
                    });
        }
        return entity;
    }

    @Override
    public CenterDto fromEntity(CenterEntity entity) {
        CenterDto dto = super.fromEntity(entity);
        dto.setLocations(new HashSet<>());
        if (entity.getLocations() != null) {
            entity.getLocations().stream()
                    .forEach(locationEntity -> {
                        LocationDto locationDto = map(locationEntity, LocationDto.class);
                        dto.getLocations().add(locationDto);
                    });
        }
        return dto;
    }
}
