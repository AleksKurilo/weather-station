package com.akurilo.weatherstation.mapper;

import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.entity.LocationEntity;
import dto.CenterDto;

import java.util.HashSet;

public class CenterMapper extends BaseMapper<CenterEntity, CenterDto> {

    @Override
    public CenterEntity toEntity(CenterDto dto) {
        CenterEntity entity = super.toEntity(dto);
        entity.setLocations(new HashSet<>());
        dto.getLocationIds().stream()
                .forEach( id ->{
                    LocationEntity locationEntity = new LocationEntity();
                    locationEntity.setId(id);
                    entity.getLocations().add(locationEntity);
                });
        return entity;
    }
}
