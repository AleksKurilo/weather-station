package com.akurilo.weatherstation.mapper;

import com.akurilo.weatherstation.entity.UserEntity;
import dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper<UserEntity, UserDto> {

    @Override
    public UserDto fromEntity(UserEntity userEntity) {
        UserDto userDto = super.fromEntity(userEntity);
        userDto.setPassword(null);
        return userDto;
    }
}
