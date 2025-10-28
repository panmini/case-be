package com.thy.route.data.entity;

import com.thy.data.entity.BaseEntity;
import com.thy.route.constant.Schemas;
import com.thy.route.constant.Tables;
import com.thy.route.data.dto.TransportationDTO;
import com.thy.route.enums.TransportationType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = Tables.TRANSPORTATION, schema = Schemas.THY)
@Data
@EqualsAndHashCode(
        callSuper = true,
        of = {})
@ToString(callSuper = true)
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class Transportation extends BaseEntity<UUID> {


    @NotNull(message = "Origin location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_location_id", nullable = false)
    private Location origin;

    @NotNull(message = "Destination location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_location_id", nullable = false)
    private Location destination;

    @NotNull(message = "Transportation type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transportation_type", nullable = false)
    private TransportationType transportationType;

    @Valid
    @ElementCollection
    @CollectionTable(name = "transportation_operating_days", schema = "thy")
    @Column(name = "day_of_week")
    private List<@Min(1) @Max(7) Integer> operatingDays = new ArrayList<>();

    public Transportation(Location origin, Location destination, TransportationType transportationType) {
        this.origin = origin;
        this.destination = destination;
        this.transportationType = transportationType;
    }

    public Transportation(Location origin, Location destination,
                          TransportationType transportationType, List<Integer> operatingDays) {
        this.origin = origin;
        this.destination = destination;
        this.transportationType = transportationType;
        this.operatingDays = operatingDays;
    }

    public Transportation(TransportationDTO transportation) {
        this.setId(transportation.getId());
        this.origin = new Location(transportation.getOrigin());
        this.destination = new Location(transportation.getDestination());
        this.transportationType = transportation.getTransportationType();
        this.operatingDays = transportation.getOperatingDays();
    }
}