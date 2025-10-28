package com.thy.route.service.impl;

import com.thy.route.data.dto.TransportationDTO;
import com.thy.route.data.entity.Transportation;
import com.thy.route.exception.EntityNotFoundException;
import com.thy.route.repository.mapper.LocationMapper;
import com.thy.route.repository.mapper.TransportationMapper;
import com.thy.route.repository.transportation.TransportationRepository;
import com.thy.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransportationService extends BaseService<Transportation, TransportationDTO, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationService.class);

    private final TransportationRepository transportationRepository;
    private final TransportationMapper transportationMapper;
    private final LocationMapper locationMapper;


    public TransportationDTO create(TransportationDTO dto) {
        Transportation entity = transportationMapper.toEntity(dto);
        Transportation saved = transportationRepository.save(entity);
        return transportationMapper.toDTO(saved);
    }


    public void delete(UUID id) {
        if (!transportationRepository.existsById(id)) {
            throw new EntityNotFoundException("Transportation", "id", id.toString());
        }
        transportationRepository.deleteById(id);
        transportationRepository.flushAndClear();
    }
}
