package com.akurilo.weatherstation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "location", schema = "weather_station", catalog = "")
@EqualsAndHashCode
public class LocationEntity  extends BaseEntity{

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "coordinates")
    private String coordinates;

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = true)
    private CenterEntity center;
}
