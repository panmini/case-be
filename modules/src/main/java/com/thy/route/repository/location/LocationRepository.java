package com.thy.route.repository.location;

import com.thy.repository.base.BaseRepository;
import com.thy.route.data.entity.Location;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends BaseRepository<Location, UUID>, CustomLocationRepository {
    Optional<Location> findByLocationCode(String locationCode);

    boolean existsByLocationCode(String locationCode);

}
