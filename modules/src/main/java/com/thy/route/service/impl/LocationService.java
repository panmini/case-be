package com.thy.route.service.impl;

import com.thy.route.data.dto.LocationDTO;
import com.thy.route.data.entity.Location;
import com.thy.route.enums.LocationType;
import com.thy.route.exception.EntityNotFoundException;
import com.thy.route.repository.location.LocationRepository;
import com.thy.route.repository.mapper.LocationMapper;
import com.thy.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService extends BaseService<Location, LocationDTO, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Transactional(readOnly = true)
    public List<LocationDTO> findAll() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::toDTO)
                .toList();
    }

    @Override
    public LocationDTO save(LocationDTO dto) {
        validateLocation(dto);
        Location location = locationMapper.toEntity(dto);
        Location saved = locationRepository.save(location);
        return locationMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public LocationDTO update(LocationDTO dto) {
        Location existing = locationMapper.toEntity(dto);

        validateLocation(dto);

        Location updated = locationRepository.save(existing);
        return locationMapper.toDTO(updated);
    }

    public void delete(UUID id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location", "id", id.toString());
        }
        locationRepository.deleteById(id);
        locationRepository.flushAndClear();
    }

    private void validateLocation(LocationDTO dto) {
        if (dto.getLocationType() == LocationType.AIRPORT && dto.getLocationCode().length() != 3) {
            throw new IllegalArgumentException("AIRPORT location code cannot be longer than 3 characters");
        }
    }
}
