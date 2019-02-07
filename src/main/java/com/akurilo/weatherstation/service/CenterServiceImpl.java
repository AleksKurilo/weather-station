package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.repository.CenterRepository;
import com.akurilo.weatherstation.repository.LocationRepository;
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
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public CenterEntity create(CenterEntity entity) {
        Set<LocationEntity> locations = entity.getLocations().stream()
                .map(locationNew -> {
                    locationRepository.save(locationNew);
                    return locationNew;
                })
                .collect(Collectors.toSet());
        entity.setLocations(locations);
        return centerRepository.save(entity);
    }

    @Override
    @Transactional
    public CenterEntity update(CenterEntity entity) {
        entity.getLocations()
                .stream()
                .map(locationEntity -> {
                    final long locationId = locationEntity.getId();
                    locationEntity = locationRepository.findById(locationId)
                            .orElseThrow(() -> new NotFoundException(locationId, LocationEntity.class));
                    locationRepository.save(locationEntity);
                    return locationEntity;
                })
                .collect(Collectors.toSet());
        return centerRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterEntity getById(long id) {
        return centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, CenterEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<CenterEntity> getList() {
        return StreamSupport.stream(centerRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public CenterEntity delete(long id) {
        CenterEntity center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, CenterEntity.class));
        centerRepository.delete(center);
        return center;
    }
}
