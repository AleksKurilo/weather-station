package com.akurilo.weatherstation.repository;

import com.akurilo.weatherstation.entity.CenterEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends CrudRepository<CenterEntity, Long> {
}
