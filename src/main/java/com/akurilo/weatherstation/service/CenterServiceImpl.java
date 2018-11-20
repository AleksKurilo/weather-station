package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.CenterEntity;
import com.akurilo.weatherstation.repository.CenterRepository;
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
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;

    @Override
    @Transactional
    public Optional<CenterEntity> create(CenterEntity entity) {
        return Optional.of(centerRepository.save(entity));
    }

    @Override
    @Transactional
    public Optional<CenterEntity> update(CenterEntity entity) {
        return Optional.of(centerRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CenterEntity> getById(long id) {
        return centerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<CenterEntity> getList() {
        return StreamSupport.stream(centerRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public Optional<CenterEntity> delete(long id) {
        Optional<CenterEntity> center = centerRepository.findById(id);
        center.ifPresent(centerEntity -> centerRepository.deleteById(id));
        return center;
    }
}
