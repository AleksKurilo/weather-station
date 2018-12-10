package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.BaseEntity;

import java.util.stream.Stream;

public interface Service<T extends BaseEntity> {

    T create(T entity);

    T update(T entity);

    T getById(long id);

    Stream<T> getList();

    T delete(long id);

}
