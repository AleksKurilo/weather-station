package com.akurilo.weatherstation.entity;

import enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "user", schema = "weather_station", catalog = "")
@EqualsAndHashCode
public class UserEntity extends BaseEntity {

    @Email
    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "create_on")
    private Timestamp createOn;

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
