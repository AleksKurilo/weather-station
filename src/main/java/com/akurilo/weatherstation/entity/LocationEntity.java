package com.akurilo.weatherstation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "location", schema = "weather_station", catalog = "")
@EqualsAndHashCode(exclude = {"stations"}, callSuper = true)
@ToString(exclude = {"stations"})
public class LocationEntity  extends BaseEntity{

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "coordinates")
    private String coordinates;

    @OneToMany(mappedBy = "location")
    private Set<StationEntity> stations;
}
