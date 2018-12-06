package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.entity.UserEntity;
import com.akurilo.weatherstation.repository.UserRepository;
import exception.NotFoundException;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Optional<UserEntity> create(UserEntity entity) {
        return Optional.of(userRepository.save(entity));
    }

    @Override
    @Transactional
    public Optional<UserEntity> update(UserEntity entity) {
        userRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException(entity.getId(), UserEntity.class));
        return Optional.of(userRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> getById(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, UserEntity.class));
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<UserEntity> getList() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public Optional<UserEntity> delete(long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, StationEntity.class));
        userRepository.delete(user);
        return Optional.of(user);
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return Optional.of(userRepository.findByEmail(email));
    }
}
