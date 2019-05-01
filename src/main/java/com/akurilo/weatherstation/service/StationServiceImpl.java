package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.mapper.StationMapper;
import com.akurilo.weatherstation.repository.LocationRepository;
import com.akurilo.weatherstation.repository.StationRepository;
import dto.StationDto;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final LocationRepository locationRepository;
    private final StationMapper stationMapper;

    @Override
    @Transactional
    public StationDto create(StationEntity entity) {
        entity = stationRepository.save(entity);
        return stationMapper.fromEntity(entity);
    }

    @Override
    @Transactional
    public StationDto update(StationEntity entity) {
        stationRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException(entity.getId(), StationEntity.class));
        StationEntity entitySaved = stationRepository.save(entity);
        return stationMapper.fromEntity(entitySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public StationDto getById(long id) {
        return stationRepository.findById(id)
                .map(stationMapper::fromEntity)
                .orElseThrow(() -> new NotFoundException(id, StationEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StationDto> getList() {
        List<StationDto> stationDtos = new ArrayList<>();
        stationRepository.findAll().forEach(stationEntity -> {
            StationDto stationDto = stationMapper.fromEntity(stationEntity);
            stationDtos.add(stationDto);
        });
        return stationDtos;
    }

    @Override
    @Transactional
    public StationDto delete(long id) {
        StationEntity station = stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, StationEntity.class));
        stationRepository.delete(station);
        return stationMapper.fromEntity(station);
    }
}
