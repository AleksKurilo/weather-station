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

    @Column(name = "name")
    private String name;

    @Column(name = "temperatureC")
    private Double temperatureC;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "pressure")
    private Integer pressure;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(name = "wind_direction")
    @Enumerated(EnumType.STRING)
    private WindDirection windDirection;

    @NotNull
    @Column(name = "coordinate")
    private String coordinate;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = true)
    private LocationEntity location;
}
