package com.akurilo.weatherstation.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "location_center", schema = "weather_station", catalog = "")
public class LocationCenterEntity extends BaseEntity {

    @Basic
    @Column(name = "center_id")
    private Long center;

    @Basic
    @Column(name = "location_id")
    private Long location;
}
