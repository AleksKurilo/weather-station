package com.akurilo.weatherstation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "center")
@Table(name = "center", schema = "weather_station", catalog = "")
@EqualsAndHashCode(exclude = {"locations"}, callSuper = true)
@ToString(exclude = {"locations"})
public class CenterEntity extends BaseEntity {

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "location_center",
            joinColumns = {@JoinColumn(name = "center_id")},
            inverseJoinColumns = {@JoinColumn(name = "location_id")}
    )
    private Set<LocationEntity> locations = new HashSet<>();
}
