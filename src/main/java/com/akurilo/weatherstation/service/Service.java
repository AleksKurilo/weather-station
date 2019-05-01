package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.BaseEntity;
import dto.BaseEntityDto;

import java.util.List;

public interface Service<T extends BaseEntity, V extends BaseEntityDto> {

    V create(T entity);

    V update(T entity);

    V getById(long id);

    List<V> getList();

    V delete(long id);

}
