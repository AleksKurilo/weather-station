package com.akurilo.weatherstation.mapper;

import com.akurilo.weatherstation.entity.BaseEntity;
import dto.BaseEntityDto;
import org.modelmapper.ModelMapper;

import java.lang.reflect.ParameterizedType;

public class BaseMapper <T extends BaseEntity, V extends BaseEntityDto> extends ModelMapper{

    private final Class<T> entityType;
    private final Class<V> dtoType;

    public BaseMapper(){
        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.dtoType = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public T toEntity(V dto){
        return map(dto, entityType);
    }

    public V fromEntity(T entity){
        return map(entity, dtoType);
    }
}
