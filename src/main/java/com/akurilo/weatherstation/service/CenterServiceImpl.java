package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.repository.CenterRepository;
import com.akurilo.weatherstation.repository.LocationRepository;
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
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public Optional<CenterEntity> create(CenterEntity entity) {
        Set<LocationEntity> locations = entity.getLocations().stream()
                .map(locationNew -> {
                    locationRepository.save(locationNew);
                    return locationNew;
                })
                .collect(Collectors.toSet());
        entity.setLocations(locations);
        return Optional.of(centerRepository.save(entity));
    }

    @Override
    @Transactional
    public Optional<CenterEntity> update(CenterEntity entity) {
        Set<LocationEntity> locations = entity.getLocations().stream()
                .map(locationEntity -> {
                    locationEntity = locationRepository.findById(locationEntity.getId()).get();
                    locationRepository.save(locationEntity);
                    return locationEntity;
                })
                .collect(Collectors.toSet());
        entity.setLocations(locations); //TODO lazy initialization exception turn on EAGER
        return Optional.of(centerRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CenterEntity> getById(long id) {
        Optional<CenterEntity> center = centerRepository.findById(id);
        center.get().getLocations().size();  //TODO lazy initialization added this method
        return center;
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<CenterEntity> getList() {
        centerRepository.findAll().forEach(entity -> entity.getLocations().size()); //TODO lazy initialization added this method
        return StreamSupport.stream(centerRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public Optional<CenterEntity> delete(long id) {
        Optional<CenterEntity> center = centerRepository.findById(id);
        center.ifPresent(locationEntity -> centerRepository.deleteById(id));
        return center;
    }
}
