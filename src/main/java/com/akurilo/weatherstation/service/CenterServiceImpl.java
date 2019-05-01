package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.entity.LocationEntity;
import com.akurilo.weatherstation.mapper.CenterMapper;
import com.akurilo.weatherstation.repository.CenterRepository;
import com.akurilo.weatherstation.repository.LocationRepository;
import dto.CenterDto;
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
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final LocationRepository locationRepository;
    private final CenterMapper centerMapper;

    @Override
    @Transactional
    public CenterDto create(CenterEntity entity) {
        Set<LocationEntity> locations = entity.getLocations().stream()
                .map(locationRepository::save)
                .collect(Collectors.toSet());
        entity.setLocations(locations);
        entity = centerRepository.save(entity);
        return centerMapper.fromEntity(entity);
    }

    @Override
    @Transactional
    public CenterDto update(CenterEntity entity) {
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
        entity = centerRepository.save(entity);
        return centerMapper.fromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterDto getById(long id) {
        return centerRepository.findById(id)
                .map(centerMapper::fromEntity)
                .orElseThrow(() -> new NotFoundException(id, CenterEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CenterDto> getList() {
        List<CenterDto> centerDtos = new ArrayList<>();
        centerRepository.findAll()
                .forEach(centerEntity -> {
                    CenterDto centerDto = centerMapper.fromEntity(centerEntity);
                    centerDtos.add(centerDto);
                });
        return centerDtos;
    }

    @Override
    @Transactional
    public CenterDto delete(long id) {
        CenterEntity center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, CenterEntity.class));
        centerRepository.delete(center);
        return centerMapper.fromEntity(center);
    }
}
