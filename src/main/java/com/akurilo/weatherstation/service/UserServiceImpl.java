package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.StationEntity;
import com.akurilo.weatherstation.entity.UserEntity;
import com.akurilo.weatherstation.repository.UserRepository;
import exception.NotFoundException;
import exception.NotUniqueException;
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

    private final static String NOT_UNIQUE_EMAIL = "email";
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserEntity create(UserEntity entity) {
        if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
            throw new NotUniqueException(NOT_UNIQUE_EMAIL);
        }
        return userRepository.save(entity);
    }

    @Override
    @Transactional
    public UserEntity update(UserEntity entity) {
        UserEntity existUser = userRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException(entity.getId(), UserEntity.class));

        boolean isNewEmailExist = userRepository.findByEmail(entity.getEmail()).isPresent();
        boolean isEmailEquals = existUser.getEmail().equals(entity.getEmail());
        if (!isEmailEquals && isNewEmailExist) {
            throw new NotUniqueException(NOT_UNIQUE_EMAIL);
        }
        return userRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, UserEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<UserEntity> getList() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false);
    }

    @Override
    @Transactional
    public UserEntity delete(long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, StationEntity.class));
        userRepository.delete(user);
        return user;
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
