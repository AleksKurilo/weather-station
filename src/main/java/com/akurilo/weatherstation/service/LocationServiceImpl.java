package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.repository.LocationRepository;
import com.akurilo.weatherstation.repository.StationRepository;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final StationRepository stationRepository;

    @Override
    @Transactional
    public LocationEntity create(LocationEntity entity) {
        return locationRepository.save(entity);
    }

    @Override
    @Transactional
    public LocationEntity update(LocationEntity entity) {
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
        return locationRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationEntity getById(long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, LocationEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<LocationEntity> getList() {
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public LocationEntity delete(long id) {
        LocationEntity location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, LocationEntity.class));
        locationRepository.delete(location);
        return location;
    }
}
