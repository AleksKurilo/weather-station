package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.mapper.LocationMapper;
import com.akurilo.weatherstation.repository.LocationRepository;
import com.akurilo.weatherstation.repository.StationRepository;
import dto.LocationDto;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final StationRepository stationRepository;
    private final LocationMapper locationMapper;

    @Override
    @Transactional
    public LocationDto create(LocationEntity entity) {
        entity = locationRepository.save(entity);
        return locationMapper.fromEntity(entity);

    }

    @Override
    @Transactional
    public LocationDto update(LocationEntity entity) {
        Set<StationEntity> stations = entity.getStations().stream()
                .map(stationEntity -> {
                    stationEntity = stationRepository.findById(stationEntity.getId())
                            .orElseThrow(() -> new NotFoundException(entity.getId(), LocationEntity.class));
                    stationEntity.setLocation(entity);
                    stationRepository.save(stationEntity);
                    return stationEntity;
                })
                .collect(Collectors.toSet());
        entity.setStations(stations);
        LocationEntity entitySaved = locationRepository.save(entity);
        return locationMapper.fromEntity(entitySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationDto getById(long id) {
        return locationRepository.findById(id)
                .map(locationMapper::fromEntity)
                .orElseThrow(() -> new NotFoundException(id, LocationEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> getList() {
        List<LocationDto> locationDtos = new ArrayList<>();
        locationRepository.findAll().forEach(locationEntity -> {
            LocationDto locationDto = locationMapper.fromEntity(locationEntity);
            locationDtos.add(locationDto);
        });
        return locationDtos;
    }

    @Override
    @Transactional
    public LocationDto delete(long id) {
        LocationEntity location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, LocationEntity.class));
        locationRepository.delete(location);
        return locationMapper.fromEntity(location);
    }
}
