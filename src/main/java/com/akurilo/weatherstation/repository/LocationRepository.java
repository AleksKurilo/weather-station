package com.akurilo.weatherstation.repository;

import com.akurilo.weatherstation.entity.LocationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
}
