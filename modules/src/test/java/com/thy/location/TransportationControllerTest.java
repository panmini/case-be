package com.thy.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thy.TestContainerSupport;
import com.thy.ThyApplication;
import com.thy.route.controller.TransportationController;
import com.thy.route.data.dto.LocationDTO;
import com.thy.route.data.dto.TransportationDTO;
import com.thy.route.data.entity.Location;
import com.thy.route.enums.LocationType;
import com.thy.route.enums.TransportationType;
import com.thy.route.repository.location.LocationRepository;
import com.thy.route.repository.transportation.TransportationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ThyApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TransportationControllerTest implements TestContainerSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransportationRepository transportationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TransportationController transportationController;

    private Location originLocation;
    private Location destinationLocation;
    private TransportationDTO testTransportationDTO;

    @BeforeEach
    void setUp() {
        transportationRepository.deleteAll();
        locationRepository.deleteAll();

        originLocation = new Location();
        originLocation.setName("Origin Airport");
        originLocation.setCountry("Country A");
        originLocation.setCity("City A");
        originLocation.setLocationCode("ORG");
        originLocation.setLocationType(LocationType.AIRPORT);
        originLocation = locationRepository.save(originLocation);

        destinationLocation = new Location();
        destinationLocation.setName("Destination Airport");
        destinationLocation.setCountry("Country B");
        destinationLocation.setCity("City B");
        destinationLocation.setLocationCode("DST");
        destinationLocation.setLocationType(LocationType.AIRPORT);
        destinationLocation = locationRepository.save(destinationLocation);

        testTransportationDTO = new TransportationDTO();
        testTransportationDTO.setOrigin(new LocationDTO(originLocation));
        testTransportationDTO.setDestination(new LocationDTO(destinationLocation));
        testTransportationDTO.setTransportationType(TransportationType.FLIGHT);
        testTransportationDTO.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5)); // Weekdays
    }

    @Test
    @Transactional
    void testCreateTransportation() {
        TransportationDTO savedDTO = transportationController.createTransportation(testTransportationDTO).getBody();

        assertNotNull(savedDTO.getId());
        assertEquals(TransportationType.FLIGHT, savedDTO.getTransportationType());
        assertEquals("ORG", savedDTO.getOrigin().getLocationCode());
        assertEquals("DST", savedDTO.getDestination().getLocationCode());
    }

    @Test
    @Transactional
    void testGetTransportationById() {
        TransportationDTO savedDTO = transportationController.createTransportation(testTransportationDTO).getBody();

        var response = transportationController.findById(savedDTO.getId());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(TransportationType.FLIGHT, response.getBody().getTransportationType());
    }

    @Test
    @Transactional
    void testGetAllTransportations() {
        transportationController.createTransportation(testTransportationDTO);

        var transportations = transportationController.getAllTransportations();
        assertFalse(transportations.isEmpty());
        assertEquals(1, transportations.size());
    }

    @Test
    @Transactional
    void testUpdateTransportation() {
        TransportationDTO savedDTO = transportationController.createTransportation(testTransportationDTO).getBody();

        savedDTO.setTransportationType(TransportationType.BUS);

        var response = transportationController.update(savedDTO.getId(), savedDTO);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(TransportationType.BUS, response.getBody().getTransportationType());
    }

    @Test
    @Transactional
    void testDeleteTransportation() {
        TransportationDTO savedDTO = transportationController.createTransportation(testTransportationDTO).getBody();

        var response = transportationController.delete(savedDTO.getId());
        assertTrue(response.getStatusCode().is2xxSuccessful());

        assertThrows(NoSuchElementException.class, () -> transportationController.findById(savedDTO.getId()));
    }

    @Test
    @Transactional
    void testCreateTransportationOverHTTP() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(testTransportationDTO);

        this.mockMvc
                .perform(
                        post("/api/transportations")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transportationType").value("FLIGHT"))
                .andExpect(jsonPath("$.origin.locationCode").value("ORG"))
                .andExpect(jsonPath("$.destination.locationCode").value("DST"));
    }

    @Test
    @Transactional
    void testGetTransportationOverHTTP() throws Exception {
        TransportationDTO savedDTO = transportationController.createTransportation(testTransportationDTO).getBody();

        this.mockMvc
                .perform(
                        get("/api/transportations/{id}", savedDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedDTO.getId().toString()))
                .andExpect(jsonPath("$.transportationType").value("FLIGHT"));
    }
}
