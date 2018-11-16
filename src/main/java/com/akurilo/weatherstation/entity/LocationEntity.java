package com.akurilo.weatherstation.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "location", schema = "weather_station", catalog = "")
public class LocationEntity {
    private Long id;
    private String name;
    private String coordinates;
    private CenterEntity centreByCentreId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "coordinates")
    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationEntity that = (LocationEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates);
    }

    @ManyToOne
    @JoinColumn(name = "centre_id", referencedColumnName = "id", nullable = false)
    public CenterEntity getCentreByCentreId() {
        return centreByCentreId;
    }

    public void setCentreByCentreId(CenterEntity centreByCentreId) {
        this.centreByCentreId = centreByCentreId;
    }
}
