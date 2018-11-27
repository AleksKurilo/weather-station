package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.UserEntity;

import java.util.Optional;

public interface UserService extends Service<UserEntity> {

    Optional<UserEntity> getByEmail(String email);
}
