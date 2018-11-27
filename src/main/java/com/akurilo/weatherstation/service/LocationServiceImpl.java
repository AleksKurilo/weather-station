package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.repository.LocationRepository;
import com.akurilo.weatherstation.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
    public Optional<LocationEntity> create(LocationEntity entity) {
        return Optional.of(locationRepository.save(entity));
    }

    @Override
    @Transactional
    public Optional<LocationEntity> update(LocationEntity entity) {
        Set<StationEntity> stations = entity.getStations().stream()
                .map(stationEntity -> {
                    stationEntity = stationRepository.findById(stationEntity.getId()).get();
                    stationEntity.setLocation(entity);
                    stationRepository.save(stationEntity);
                    return stationEntity;
                })
                .collect(Collectors.toSet());
        entity.setStations(stations);
        return Optional.of(locationRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationEntity> getById(long id) {
        return locationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<LocationEntity> getList() {
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public Optional<LocationEntity> delete(long id) {
        Optional<LocationEntity> location = locationRepository.findById(id);
        location.ifPresent(locationEntity -> locationRepository.deleteById(id));
        return location;
    }
}
