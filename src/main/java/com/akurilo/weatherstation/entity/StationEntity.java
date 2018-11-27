package com.akurilo.weatherstation.entity;

import enums.WindDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@Entity
@Table(name = "station", schema = "weather_station", catalog = "")
@EqualsAndHashCode(callSuper = true)
@ToString
public class StationEntity extends BaseEntity {

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

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = true)
    private LocationEntity location;
}
