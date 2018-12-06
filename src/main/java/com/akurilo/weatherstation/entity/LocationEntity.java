package com.akurilo.weatherstation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "location", schema = "weather_station", catalog = "")
@EqualsAndHashCode(exclude = {"stations"}, callSuper = true)
@ToString(exclude = {"stations"})
public class LocationEntity  extends BaseEntity{

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "coordinates")
    private String coordinates;

    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    private Set<StationEntity> stations;

    @ManyToMany
    @JoinTable(
            name = "location_center",
            joinColumns = {@JoinColumn(name = "location_id")},
            inverseJoinColumns = {@JoinColumn(name = "center_id")}
    )
    Set<CenterEntity> centers = new HashSet<>();
}
