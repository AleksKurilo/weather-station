package com.akurilo.weatherstation.entity;

import enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "user", schema = "weather_station", catalog = "")
@EqualsAndHashCode
public class UserEntity extends BaseEntity {

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Basic
    @Column(name = "create_on")
    private Timestamp createOn;

    @Basic
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @PrePersist
    private void onCreate() {
        createOn = new Timestamp(new Date().getTime());
    }

    @PreUpdate
    private void onUpdate() {
        lastUpdate = new Timestamp(new Date().getTime());
    }
}
