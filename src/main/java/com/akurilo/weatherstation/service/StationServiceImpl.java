package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.repository.LocationRepository;
import com.akurilo.weatherstation.repository.StationRepository;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public StationEntity create(StationEntity entity) {
        return stationRepository.save(entity);
    }

    @Override
    @Transactional
    public StationEntity update(StationEntity entity) {
        stationRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException(entity.getId(), StationEntity.class));
        return stationRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public StationEntity getById(long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, StationEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<StationEntity> getList() {
        return StreamSupport.stream(stationRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public StationEntity delete(long id) {
        StationEntity station = stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, StationEntity.class));
        stationRepository.delete(station);
        return station;
    }
}
