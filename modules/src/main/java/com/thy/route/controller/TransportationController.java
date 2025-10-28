package com.thy.route.controller;

import com.thy.controller.BaseController;
import com.thy.route.data.dto.TransportationDTO;
import com.thy.route.data.entity.Transportation;
import com.thy.route.service.impl.TransportationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transportations")
@RequiredArgsConstructor
public class TransportationController extends BaseController<Transportation, TransportationDTO, UUID, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationController.class);

    private final TransportationService transportationService;

    @GetMapping
    public List<TransportationDTO> getAllTransportations() {
        return transportationService.findAll();
    }

    @PostMapping
    public ResponseEntity<TransportationDTO> createTransportation(@RequestBody TransportationDTO transportationDTO) {
        return ResponseEntity.ok(transportationService.create(transportationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTransportation(@PathVariable UUID id) {
        transportationService.deleteById(id);
        transportationService.flushAndClear();
        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }
}
