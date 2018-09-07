package com.akurilo.weatherstation.service;

import com.akurilo.weatherstation.dto.BasetEntityDto;

import java.util.List;

public interface Service<T extends BasetEntityDto> {

    T create(T entity);

    T update(T entity);

    T getById(long id);

    List<T> getList();

    T delete(long id);

}
