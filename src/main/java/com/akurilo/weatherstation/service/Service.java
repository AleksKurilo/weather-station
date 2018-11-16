package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.entity.BaseEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface Service<T extends BaseEntity> {

    Optional<T> create(T entity);

    Optional<T> update(T entity);

    Optional<T> getById(long id);

    Stream<T> getList();

    Optional<T> delete(long id);

}
