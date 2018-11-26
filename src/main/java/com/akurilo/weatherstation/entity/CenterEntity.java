package com.akurilo.weatherstation.entity;

import enums.WindDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@Table(name = "center", schema = "weather_station", catalog = "")
@EqualsAndHashCode(exclude = {"locations"})
@ToString(exclude = {"locations"})
public class CenterEntity extends BaseEntity {

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "temperatureC")
    private Double temperatureC;

    @Basic
    @Column(name = "humidity")
    private Double humidity;

    @Basic
    @Column(name = "pressure")
    private Integer pressure;

    @Basic
    @Column(name = "wind_speed")
    private Double windSpeed;

    @Basic
    @Column(name = "wind_direction")
    @Enumerated(EnumType.STRING)
    private WindDirection windDirection;

    @Basic
    @Column(name = "coordinates")
    @NotNull
    private String coordinates;

    @OneToMany(mappedBy = "center")
    private Set<LocationEntity> locations;
}
