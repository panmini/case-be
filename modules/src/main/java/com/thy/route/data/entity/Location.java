package com.thy.route.data.entity;

import com.thy.data.entity.BaseEntity;
import com.thy.route.constant.Schemas;
import com.thy.route.constant.Tables;
import com.thy.route.data.dto.LocationDTO;
import com.thy.route.enums.LocationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = Tables.LOCATION, schema = Schemas.THY)
@Data
@EqualsAndHashCode(
        callSuper = true,
        of = {})
@ToString(callSuper = true)
@SQLRestriction("deleted = false")
public class Location extends BaseEntity<UUID> {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Country is required")
    @Size(max = 255, message = "Country cannot exceed 255 characters")
    @Column(nullable = false)
    private String country;

    @NotNull(message = "Location type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = false)
    private LocationType locationType;

    @NotBlank(message = "City is required")
    @Size(max = 255, message = "City cannot exceed 255 characters")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "Location code is required")
    @Pattern(regexp = "^[A-Z0-9]{1,5}$", message = "Location code must be 1 to 5 uppercase letters or digits")
    @Column(name = "location_code", nullable = false, unique = true, length = 5)
    private String locationCode;

    public Location() {
    }

    public Location(String name, String country, String city, String locationCode, LocationType locationType) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.locationCode = locationCode;
        this.locationType = locationType;
    }

    public Location(LocationDTO location) {
        this.setId(location.getId());
        this.name = location.getName();
        this.country = location.getCountry();
        this.city = location.getCity();
        this.locationCode = location.getLocationCode();
        this.locationType = location.getLocationType();
    }
}