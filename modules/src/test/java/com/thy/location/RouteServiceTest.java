package com.thy.location;

import com.thy.TestContainerSupport;
import com.thy.ThyApplication;
import com.thy.route.data.dto.RouteDTO;
import com.thy.route.data.entity.Location;
import com.thy.route.data.entity.Transportation;
import com.thy.route.enums.LocationType;
import com.thy.route.enums.TransportationType;
import com.thy.route.repository.location.LocationRepository;
import com.thy.route.repository.transportation.TransportationRepository;
import com.thy.route.service.impl.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ThyApplication.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RouteServiceTest implements TestContainerSupport {

    @Autowired
    private RouteService routeService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TransportationRepository transportationRepository;

    private Location origin, destination, intermediate1, intermediate2;

    @BeforeEach
    void setUp() {
        transportationRepository.deleteAll();
        locationRepository.deleteAll();

        origin = locationRepository.save(new Location("City Center", "Testland", "Test City", "ORG", LocationType.OTHER));
        intermediate1 = locationRepository.save(new Location("Airport A", "Testland", "Test City", "APT", LocationType.AIRPORT));
        intermediate2 = locationRepository.save(new Location("Airport B", "Testland", "Test City B", "INT", LocationType.AIRPORT));
        destination = locationRepository.save(new Location("Destination Center", "Testland", "Dest City", "DES", LocationType.OTHER));
    }

    @Test
    @Transactional
    void testDirectFlightRoute() {
        transportationRepository.save(
                new Transportation(origin, destination, TransportationType.FLIGHT, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        List<RouteDTO> routes = routeService.findValidRoutes("ORG", "DES", LocalDate.now());

        assertEquals(1, routes.size());
        assertEquals(1, routes.get(0).getTransportations().size());
        assertEquals(TransportationType.FLIGHT, routes.get(0).getTransportations().get(0).getTransportationType());
    }

    @Test
    @Transactional
    void testBeforeTransferAndFlightRoute() {
        transportationRepository.save(
                new Transportation(origin, intermediate1, TransportationType.UBER, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        transportationRepository.save(
                new Transportation(intermediate1, destination, TransportationType.FLIGHT, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        List<RouteDTO> routes = routeService.findValidRoutes("ORG", "DES", LocalDate.now());

        assertFalse(routes.isEmpty());

        boolean foundBeforeTransferRoute = routes.stream()
                .anyMatch(route -> route.getTransportations().size() == 2 &&
                        route.getTransportations().get(0).getTransportationType() == TransportationType.UBER);
        assertTrue(foundBeforeTransferRoute);
    }

    @Test
    @Transactional
    void testOperatingDaysFilter() {
        transportationRepository.save(
                new Transportation(origin, intermediate1, TransportationType.BUS, Arrays.asList(1, 2, 3, 4, 5)) // Weekdays only
        );

        transportationRepository.save(
                new Transportation(intermediate1, destination, TransportationType.FLIGHT, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        int nextYear = LocalDate.now().getYear() + 1;

        LocalDate firstDayOfNextYear = LocalDate.of(nextYear, 1, 1);
        LocalDate wednesday = firstDayOfNextYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));

        LocalDate sunday = firstDayOfNextYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<RouteDTO> wednesdayRoutes = routeService.findValidRoutes("ORG", "DES", wednesday);
        List<RouteDTO> sundayRoutes = routeService.findValidRoutes("ORG", "DES", sunday);

        assertTrue(wednesdayRoutes.size() > sundayRoutes.size());
    }

    @Test
    @Transactional
    void testInvalidRoutesAreFiltered() {
        transportationRepository.save(
                new Transportation(origin, intermediate1, TransportationType.UBER, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );
        transportationRepository.save(
                new Transportation(origin, intermediate1, TransportationType.BUS, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        transportationRepository.save(
                new Transportation(intermediate1, destination, TransportationType.FLIGHT, Arrays.asList(1, 2, 3, 4, 5, 6, 7))
        );

        List<RouteDTO> routes = routeService.findValidRoutes("ORG", "DES", LocalDate.now());

        long beforeTransferRoutes = routes.stream()
                .filter(route -> route.getTransportations().size() == 2)
                .count();
        assertEquals(2, beforeTransferRoutes);
    }
}