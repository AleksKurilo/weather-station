package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.UserEntity;
import com.akurilo.weatherstation.repository.UserRepository;
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
        return Optional.of(userRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> getById(long id) {
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
        Optional<UserEntity> user = userRepository.findById(id);
        user.ifPresent(userEntity -> userRepository.deleteById(id));
        return user;
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return Optional.of(userRepository.findByEmail(email));
    }
}
