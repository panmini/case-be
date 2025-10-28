package com.thy.route.data.dto;

import com.thy.data.dto.BaseDTO;
import com.thy.route.data.entity.Location;
import com.thy.route.enums.LocationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationDTO extends BaseDTO<UUID> {
    private UUID id;
    private String name;
    private String country;
    private String city;
    private String locationCode;
    private LocationType locationType;

    public LocationDTO() {
    }

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.country = location.getCountry();
        this.city = location.getCity();
        this.locationCode = location.getLocationCode();
        this.locationType = location.getLocationType();
    }

}