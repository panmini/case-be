package com.thy.route.repository.transportation;

import com.thy.repository.base.BaseRepository;
import com.thy.route.data.entity.Location;
import com.thy.route.data.entity.Transportation;
import com.thy.route.enums.TransportationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TransportationRepository extends BaseRepository<Transportation, UUID>, CustomTransportationRepository {

    List<Transportation> findByOrigin(Location origin);

    List<Transportation> findByDestination(Location destination);

    List<Transportation> findByTransportationType(TransportationType type);

    @Query("SELECT t FROM Transportation t WHERE t.origin = :origin AND t.destination = :destination")
    List<Transportation> findByOriginAndDestination(@Param("origin") Location origin,
                                                    @Param("destination") Location destination);

    List<Transportation> findByOriginLocationCodeAndDestinationLocationCode(String originCode, String destinationCode);

    List<Transportation> findByOriginAndTransportationType(Location origin, TransportationType type);

    List<Transportation> findByOriginAndTransportationTypeNot(Location origin, TransportationType type);

    List<Transportation> findByOriginInAndTransportationType(Set<Location> origins, TransportationType type);

    List<Transportation> findByOriginInAndTransportationTypeNot(Set<Location> origins, TransportationType type);

    List<Transportation> findByOriginInAndTransportationTypeNotAndDestination(Set<Location> origins, TransportationType type, Location destination);

    @Query("SELECT t FROM Transportation t WHERE t.origin = :origin AND t.destination = :destination AND t.transportationType = :type")
    List<Transportation> findByOriginAndDestinationAndTransportationType(
            @Param("origin") Location origin,
            @Param("destination") Location destination,
            @Param("type") TransportationType type);


    @Query("""
            SELECT DISTINCT t
            FROM Transportation t
            LEFT JOIN FETCH t.origin
            LEFT JOIN FETCH t.destination
            WHERE t.operatingDays IS EMPTY
               OR :dayOfWeek IN elements(t.operatingDays)
            """)
    List<Transportation> findAllOperatingOnDay(@Param("dayOfWeek") int dayOfWeek);

    @Query("SELECT t FROM Transportation t " +
            "WHERE (t.origin.id = :originId OR t.destination.id = :destinationId) " +
            "OR t.origin.id IN (SELECT t2.destination.id FROM Transportation t2 WHERE t2.origin.id = :originId) " +
            "OR t.destination.id IN (SELECT t3.origin.id FROM Transportation t3 WHERE t3.destination.id = :destinationId)")
    List<Transportation> findAllRelevantForRouteSearch(@Param("originId") UUID originId,
                                                       @Param("destinationId") UUID destinationId);

}
