package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.UserEntity;
import com.akurilo.weatherstation.mapper.UserMapper;
import com.akurilo.weatherstation.repository.UserRepository;
import dto.UserDto;
import exception.NotFoundException;
import exception.NotUniqueException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String NOT_UNIQUE_EMAIL = "email";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserEntity entity) {
        if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
            throw new NotUniqueException(NOT_UNIQUE_EMAIL);
        }
        UserEntity entitySaved = userRepository.save(entity);
        return userMapper.fromEntity(entitySaved);
    }

    @Override
    @Transactional
    public UserDto update(UserEntity entity) {
        UserEntity existUser = userRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException(entity.getId(), UserEntity.class));

        boolean isNewEmailExist = userRepository.findByEmail(entity.getEmail()).isPresent();
        boolean isEmailEquals = existUser.getEmail().equals(entity.getEmail());
        if (!isEmailEquals && isNewEmailExist) {
            throw new NotUniqueException(NOT_UNIQUE_EMAIL);
        }
        UserEntity entitySaved = userRepository.save(entity);
        return userMapper.fromEntity(entitySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(long id) {
        return userRepository.findById(id)
                .map(userMapper::fromEntity)
                .orElseThrow(() -> new NotFoundException(id, UserEntity.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getList() {
        List<UserDto> userDtos = new ArrayList<>();
        userRepository.findAll().forEach(userEntity -> {
            UserDto userDto = userMapper.fromEntity(userEntity);
            userDtos.add(userDto);
        });
        return userDtos;
    }

    @Override
    @Transactional
    public UserDto delete(long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, UserEntity.class));
        userRepository.delete(user);
        return userMapper.fromEntity(user);
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
