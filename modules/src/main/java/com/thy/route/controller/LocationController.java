package com.thy.route.controller;

import com.thy.controller.BaseController;
import com.thy.route.data.dto.LocationDTO;
import com.thy.route.data.entity.Location;
import com.thy.route.service.impl.LocationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController
        extends BaseController<Location, LocationDTO, UUID, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable UUID id) {
        locationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
