package com.akurilo.weatherstation.entity;

import enums.WindDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "center", schema = "weather_station", catalog = "")
@EqualsAndHashCode
public class CenterEntity extends BaseEntity {


    //private Long id;

    @Basic
    @Column(name = "name")
    @Max(value = 255)
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
}
