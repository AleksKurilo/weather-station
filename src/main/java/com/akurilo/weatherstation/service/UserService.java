package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.UserEntity;
import dto.UserDto;

import java.util.Optional;

public interface UserService extends Service<UserEntity, UserDto> {

    Optional<UserEntity> getByEmail(String email);
}
