package com.thy.location;

import com.thy.TestContainerSupport;
import com.thy.ThyApplication;
import com.thy.route.controller.RouteController;
import com.thy.route.data.dto.RouteDTO;
import com.thy.route.data.entity.Location;
import com.thy.route.data.entity.Transportation;
import com.thy.route.enums.LocationType;
import com.thy.route.enums.TransportationType;
import com.thy.route.exception.EntityNotFoundException;
import com.thy.route.repository.location.LocationRepository;
import com.thy.route.repository.transportation.TransportationRepository;
import com.thy.route.service.impl.RouteService;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ThyApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RouteControllerTest implements TestContainerSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteService routeService;

    @Autowired
    private RouteController routeController;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TransportationRepository transportationRepository;

    private Location origin;
    private Location destination;
    private Location intermediate;
    private Transportation directFlight;
    private Transportation beforeTransfer;
    private Transportation intermediateFlight;

    @BeforeEach
    void setUp() {
        transportationRepository.deleteAll();
        locationRepository.deleteAll();

        origin = locationRepository.save(new Location("City Center", "Country", "City", "CTR", LocationType.OTHER
        ));
        intermediate = locationRepository.save(new Location("Airport A", "Country", "City", "APT", LocationType.AIRPORT));
        destination = locationRepository.save(new Location("Airport B", "Country", "City B", "DES", LocationType.AIRPORT));

        beforeTransfer = transportationRepository.save(
                new Transportation(origin, intermediate, TransportationType.UBER, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        directFlight = transportationRepository.save(
                new Transportation(origin, destination, TransportationType.FLIGHT, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        intermediateFlight = transportationRepository.save(
                new Transportation(intermediate, destination, TransportationType.FLIGHT, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );
    }

    @Test
    @Transactional
    void testFindDirectRoutes() {
        List<RouteDTO> routes = routeController.findRoutes("CTR", "DES", LocalDate.now());

        assertFalse(routes.isEmpty());

        Optional<RouteDTO> directFlightRoute = routes.stream()
                .filter(route -> route.getTransportations().size() == 1)
                .filter(route -> route.getTransportations().get(0).getTransportationType() == TransportationType.FLIGHT)
                .findFirst();

        assertTrue(directFlightRoute.isPresent(), "Should find at least one direct flight route");
        assertEquals(TransportationType.FLIGHT, directFlightRoute.get().getTransportations().get(0).getTransportationType());

        long directFlightCount = routes.stream()
                .filter(route -> route.getTransportations().size() == 1)
                .filter(route -> route.getTransportations().get(0).getTransportationType() == TransportationType.FLIGHT)
                .count();
        assertEquals(1, directFlightCount, "Should find exactly one direct flight route");
    }

    @Test
    @Transactional
    void testFindRoutesWithBeforeTransfer() {
        List<RouteDTO> routes = routeController.findRoutes("CTR", "DES", LocalDate.now());

        assertTrue(routes.size() >= 2);

        boolean foundBeforeTransferRoute = routes.stream()
                .anyMatch(route -> route.getTransportations().size() == 2 &&
                        route.getTransportations().get(0).getTransportationType() == TransportationType.UBER);
        assertTrue(foundBeforeTransferRoute);
    }

    @Test
    @Transactional
    void testFindRoutesWithOperatingDaysFilter() {
        Transportation weekendBus = transportationRepository.save(
                new Transportation(origin, intermediate, TransportationType.BUS, Arrays.asList(6, 7))
        );

        int nextYear = LocalDate.now().getYear() + 1;

        LocalDate firstDayOfNextYear = LocalDate.of(nextYear, 1, 1);
        LocalDate monday = firstDayOfNextYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        LocalDate saturday = firstDayOfNextYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        List<RouteDTO> mondayRoutes = routeController.findRoutes("CTR", "DES", monday);
        List<RouteDTO> saturdayRoutes = routeController.findRoutes("CTR", "DES", saturday);

        assertTrue(saturdayRoutes.size() >= mondayRoutes.size());
    }

    @Test
    @Transactional
    void testFindRoutesOverHTTP() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/routes")
                                .param("origin", "CTR")
                                .param("destination", "DES")
                                .param("date", LocalDateTime.now().getYear() + 1 + "-01-01")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Transactional
    void testFindRoutesWithoutDateOverHTTP() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/routes")
                                .param("origin", "CTR")
                                .param("destination", "DES")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Transactional
    void testFindRoutesInvalidLocations() {
        List<RouteDTO> routes = routeController.findRoutes("DES", "APT", LocalDate.now());
        assertTrue(routes.isEmpty());

        assertThrows(EntityNotFoundException.class,
                () -> routeController.findRoutes("CTR", "INVALID", LocalDate.now())
        );
    }

    @Test
    @Transactional
    void testNoRoutesFound() {
        List<RouteDTO> routes = routeController.findRoutes("DES", "CTR", LocalDate.now());
        assertTrue(routes.isEmpty());
    }
}