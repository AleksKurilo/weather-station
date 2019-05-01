package com.akurilo.weatherstation.repository;

import com.akurilo.weatherstation.entity.CenterEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CenterRepository extends CrudRepository<CenterEntity, Long> {

    Optional<CenterEntity> findById(Long id);
}
