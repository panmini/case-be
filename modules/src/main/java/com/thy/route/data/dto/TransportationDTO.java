package com.thy.route.data.dto;

import com.thy.data.dto.BaseDTO;
import com.thy.route.data.entity.Transportation;
import com.thy.route.enums.TransportationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransportationDTO extends BaseDTO<UUID> {
    private UUID id;
    private LocationDTO origin;
    private LocationDTO destination;
    private TransportationType transportationType;
    private List<Integer> operatingDays;

    public TransportationDTO() {
    }

    public TransportationDTO(Transportation transportation) {
        this.id = transportation.getId();
        this.origin = new LocationDTO(transportation.getOrigin());
        this.destination = new LocationDTO(transportation.getDestination());
        this.transportationType = transportation.getTransportationType();
        this.operatingDays = transportation.getOperatingDays();
    }
}