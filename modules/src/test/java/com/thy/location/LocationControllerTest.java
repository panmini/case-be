package com.thy.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.thy.TestContainerSupport;
import com.thy.ThyApplication;
import com.thy.route.controller.LocationController;
import com.thy.route.data.dto.LocationDTO;
import com.thy.route.enums.LocationType;
import com.thy.route.repository.location.LocationRepository;
import com.thy.route.service.impl.LocationService;
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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {ThyApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LocationControllerTest implements TestContainerSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationController locationController;
    @Autowired
    private LocationService locationService;

    private LocationDTO testLocation;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();

        testLocation = new LocationDTO();
        testLocation.setId(null);
        testLocation.setName("Test Airport");
        testLocation.setCountry("Test Country");
        testLocation.setCity("Test City");
        testLocation.setLocationCode("TST");
        testLocation.setLocationType(LocationType.AIRPORT);
    }

    @Test
    @Transactional
    void testCreateLocation() {
        LocationDTO savedLocation = locationController.create(testLocation).getBody();

        assertNotNull(savedLocation.getId());
        assertEquals("TST", savedLocation.getLocationCode());
        assertEquals("Test Airport", savedLocation.getName());
    }

    @Test
    @Transactional
    void testGetLocationById() {
        LocationDTO savedLocation = locationController.create(testLocation).getBody();

        var response = locationController.findById(savedLocation.getId());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("TST", response.getBody().getLocationCode());
    }

    @Test
    @Transactional
    void testGetAllLocations() {
        locationController.create(testLocation);

        var locations = locationController.findAll().getBody();
        assertFalse(locations.isEmpty());
        assertEquals(1, locations.size());
    }

    @Test
    @Transactional
    void testUpdateLocation() {
        LocationDTO savedLocation = locationController.create(testLocation).getBody();

        savedLocation.setName("Updated Airport");
        savedLocation.setCountry("Updated Country");
        savedLocation.setCity("Updated City");
        savedLocation.setLocationCode("UPD");
        testLocation.setLocationType(LocationType.AIRPORT);

        var response = locationController.update(savedLocation.getId(), savedLocation);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("UPD", response.getBody().getLocationCode());
    }

    @Test
    @Transactional
    void testDeleteLocation() {
        LocationDTO savedLocation = locationController.create(testLocation).getBody();

        var response = locationController.deleteLocation(savedLocation.getId());
        assertTrue(response.getStatusCode().is2xxSuccessful());

        assertThrows(NoSuchElementException.class, () -> locationController.findById(savedLocation.getId()));
    }

    @Test
    @Transactional
    void testCreateLocationOverHTTP() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(testLocation);

        this.mockMvc
                .perform(
                        post("/api/locations")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.locationCode").value("TST"))
                .andExpect(jsonPath("$.name").value("Test Airport"));
    }

    @Test
    @Transactional
    void testGetLocationOverHTTP() throws Exception {
        LocationDTO savedLocation = locationService.save(testLocation);

        this.mockMvc
                .perform(
                        get("/api/locations/{id}", savedLocation.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedLocation.getId().toString()))
                .andExpect(jsonPath("$.locationCode").value("TST"));
    }

    @Test
    @Transactional
    void testGetAllLocationsOverHTTP() throws Exception {
        locationService.save(testLocation);

        this.mockMvc
                .perform(
                        get("/api/locations")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].locationCode").value("TST"));
    }

    @Test
    @Transactional
    void testUpdateLocationOverHTTP() throws Exception {
        LocationDTO savedLocation = locationService.save(testLocation);

        savedLocation.setName("Updated Airport");
        savedLocation.setCountry("Updated Country");
        savedLocation.setCity("Updated City");
        savedLocation.setLocationCode("UPD");
        testLocation.setLocationType(LocationType.AIRPORT);

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(savedLocation);

        this.mockMvc
                .perform(
                        put("/api/locations/{id}", savedLocation.getId())
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationCode").value("UPD"))
                .andExpect(jsonPath("$.name").value("Updated Airport"));
    }

    @Test
    @Transactional
    void testDeleteLocationOverHTTP() throws Exception {
        LocationDTO savedLocation = locationService.save(testLocation);

        this.mockMvc
                .perform(
                        delete("/api/locations/{id}", savedLocation.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc
                .perform(
                        get("/api/locations/{id}", savedLocation.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}