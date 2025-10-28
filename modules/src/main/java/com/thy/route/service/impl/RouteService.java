package com.thy.route.service.impl;

import com.thy.route.data.dto.RouteDTO;
import com.thy.route.data.dto.TransportationDTO;
import com.thy.route.data.entity.Location;
import com.thy.route.data.entity.Transportation;
import com.thy.route.enums.TransportationType;
import com.thy.route.exception.EntityNotFoundException;
import com.thy.route.repository.location.LocationRepository;
import com.thy.route.repository.transportation.TransportationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RouteService {

    @Autowired
    private TransportationRepository transportationRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<RouteDTO> findValidRoutes(String originCode, String destinationCode, LocalDate date) {
        validateInput(originCode, destinationCode, date);

        Location origin = locationRepository.findByLocationCode(originCode)
                .orElseThrow(() -> new EntityNotFoundException("Location", "code", originCode));
        Location destination = locationRepository.findByLocationCode(destinationCode)
                .orElseThrow(() -> new EntityNotFoundException("Location", "code", destinationCode));

        RouteSearchContext context = buildSearchContext(origin, destination, date);

        List<RouteDTO> validRoutes = new ArrayList<>();

        findDirectFlights(context).forEach(validRoutes::add);
        findRoutesWithBeforeTransfer(context).forEach(validRoutes::add);
        findRoutesWithAfterTransfer(context).forEach(validRoutes::add);
        findRoutesWithBothTransfers(context).forEach(validRoutes::add);

        return validRoutes;
    }

    private void validateInput(String originCode, String destinationCode, LocalDate date) {
        if (originCode == null || originCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin location code is required");
        }
        if (destinationCode == null || destinationCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination location code is required");
        }
        if (originCode.equals(destinationCode)) {
            throw new IllegalArgumentException("Origin and destination cannot be the same");
        }

        if (date == null) {
            date = LocalDate.now();
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
    }

    private RouteSearchContext buildSearchContext(Location origin, Location destination, LocalDate date) {
        List<Transportation> allRelevantTransportations = transportationRepository
                .findAllRelevantForRouteSearch(origin.getId(), destination.getId());

        Map<UUID, List<Transportation>> transportationsByOrigin = new HashMap<>();
        Map<UUID, List<Transportation>> transportationsByDestination = new HashMap<>();
        Map<Pair<UUID, UUID>, List<Transportation>> transportationsByOriginDestination = new HashMap<>();

        List<Transportation> operatingTransportations = allRelevantTransportations.stream()
                .filter(t -> isOperatingOnDate(t, date))
                .collect(Collectors.toList());

        for (Transportation t : operatingTransportations) {
            transportationsByOrigin
                    .computeIfAbsent(t.getOrigin().getId(), k -> new ArrayList<>())
                    .add(t);

            transportationsByDestination
                    .computeIfAbsent(t.getDestination().getId(), k -> new ArrayList<>())
                    .add(t);

            Pair<UUID, UUID> key = Pair.of(t.getOrigin().getId(), t.getDestination().getId());
            transportationsByOriginDestination
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .add(t);
        }

        return new RouteSearchContext(
                origin,
                destination,
                date,
                transportationsByOrigin,
                transportationsByDestination,
                transportationsByOriginDestination
        );
    }

    private List<RouteDTO> findDirectFlights(RouteSearchContext context) {
        Pair<UUID, UUID> key = Pair.of(context.origin().getId(), context.destination().getId());
        List<Transportation> directFlights = context.transportationsByOriginDestination()
                .getOrDefault(key, Collections.emptyList())
                .stream()
                .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                .collect(Collectors.toList());

        return directFlights.stream()
                .map(flight -> createRouteDTO(List.of(flight)))
                .collect(Collectors.toList());
    }

    private List<RouteDTO> findRoutesWithBeforeTransfer(RouteSearchContext context) {
        List<RouteDTO> routes = new ArrayList<>();

        List<Transportation> beforeTransfers = context.transportationsByOrigin()
                .getOrDefault(context.origin().getId(), Collections.emptyList())
                .stream()
                .filter(t -> t.getTransportationType() != TransportationType.FLIGHT)
                .collect(Collectors.toList());

        for (Transportation transfer : beforeTransfers) {
            Location transferPoint = transfer.getDestination();
            Pair<UUID, UUID> flightKey = Pair.of(transferPoint.getId(), context.destination().getId());

            List<Transportation> flights = context.transportationsByOriginDestination()
                    .getOrDefault(flightKey, Collections.emptyList())
                    .stream()
                    .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                    .collect(Collectors.toList());

            for (Transportation flight : flights) {
                routes.add(createRouteDTO(List.of(transfer, flight)));
            }
        }

        return routes;
    }

    private List<RouteDTO> findRoutesWithAfterTransfer(RouteSearchContext context) {
        List<RouteDTO> routes = new ArrayList<>();

        List<Transportation> flights = context.transportationsByOrigin()
                .getOrDefault(context.origin().getId(), Collections.emptyList())
                .stream()
                .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                .collect(Collectors.toList());

        for (Transportation flight : flights) {
            Location flightDestination = flight.getDestination();
            Pair<UUID, UUID> transferKey = Pair.of(flightDestination.getId(), context.destination().getId());

            List<Transportation> afterTransfers = context.transportationsByOriginDestination()
                    .getOrDefault(transferKey, Collections.emptyList())
                    .stream()
                    .filter(t -> t.getTransportationType() != TransportationType.FLIGHT)
                    .collect(Collectors.toList());

            for (Transportation transfer : afterTransfers) {
                routes.add(createRouteDTO(List.of(flight, transfer)));
            }
        }

        return routes;
    }

    private List<RouteDTO> findRoutesWithBothTransfers(RouteSearchContext context) {
        List<RouteDTO> routes = new ArrayList<>();

        List<Transportation> beforeTransfers = context.transportationsByOrigin()
                .getOrDefault(context.origin().getId(), Collections.emptyList())
                .stream()
                .filter(t -> t.getTransportationType() != TransportationType.FLIGHT)
                .collect(Collectors.toList());

        for (Transportation beforeTransfer : beforeTransfers) {
            Location intermediateAirport = beforeTransfer.getDestination();

            List<Transportation> flights = context.transportationsByOrigin()
                    .getOrDefault(intermediateAirport.getId(), Collections.emptyList())
                    .stream()
                    .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                    .collect(Collectors.toList());

            for (Transportation flight : flights) {
                Location arrivalAirport = flight.getDestination();
                Pair<UUID, UUID> transferKey = Pair.of(arrivalAirport.getId(), context.destination().getId());

                List<Transportation> afterTransfers = context.transportationsByOriginDestination()
                        .getOrDefault(transferKey, Collections.emptyList())
                        .stream()
                        .filter(t -> t.getTransportationType() != TransportationType.FLIGHT)
                        .collect(Collectors.toList());

                for (Transportation afterTransfer : afterTransfers) {
                    if (!hasDuplicateLocations(beforeTransfer, flight, afterTransfer)) {
                        routes.add(createRouteDTO(List.of(beforeTransfer, flight, afterTransfer)));
                    }
                }
            }
        }

        return routes;
    }

    private RouteDTO createRouteDTO(List<Transportation> transportations) {
        return new RouteDTO(transportations.stream()
                .map(TransportationDTO::new)
                .collect(Collectors.toList()));
    }

    private boolean hasDuplicateLocations(Transportation beforeTransfer, Transportation flight, Transportation afterTransfer) {
        Location origin = beforeTransfer.getOrigin();
        Location intermediate1 = beforeTransfer.getDestination();
        Location intermediate2 = flight.getDestination();
        Location finalDest = afterTransfer.getDestination();

        return origin.equals(intermediate1) || origin.equals(intermediate2) || origin.equals(finalDest) ||
                intermediate1.equals(intermediate2) || intermediate1.equals(finalDest) ||
                intermediate2.equals(finalDest);
    }

    private boolean isOperatingOnDate(Transportation transportation, LocalDate date) {
        if (transportation.getOperatingDays() == null || transportation.getOperatingDays().isEmpty()) {
            return true;
        }
        int dayOfWeek = date.getDayOfWeek().getValue();
        return transportation.getOperatingDays().contains(dayOfWeek);
    }

    private record RouteSearchContext(
            Location origin,
            Location destination,
            LocalDate date,
            Map<UUID, List<Transportation>> transportationsByOrigin,
            Map<UUID, List<Transportation>> transportationsByDestination,
            Map<Pair<UUID, UUID>, List<Transportation>> transportationsByOriginDestination
    ) {
    }
}