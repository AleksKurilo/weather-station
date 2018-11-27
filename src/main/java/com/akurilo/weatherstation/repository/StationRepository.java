package com.akurilo.weatherstation.repository;

import com.akurilo.weatherstation.entity.StationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends CrudRepository<StationEntity, Long> {
}
