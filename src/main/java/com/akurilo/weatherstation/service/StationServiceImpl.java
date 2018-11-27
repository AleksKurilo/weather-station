package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.repository.LocationRepository;
import com.akurilo.weatherstation.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public Optional<StationEntity> create(StationEntity entity) {
        return Optional.of(stationRepository.save(entity));
    }

    @Override
    @Transactional
    public Optional<StationEntity> update(StationEntity entity) {
        return Optional.of(stationRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StationEntity> getById(long id) {
        return stationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<StationEntity> getList() {
        return StreamSupport.stream(stationRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public Optional<StationEntity> delete(long id) {
        Optional<StationEntity> station = stationRepository.findById(id);
        station.ifPresent(stationEntity -> stationRepository.deleteById(id));
        return station;
    }
}
