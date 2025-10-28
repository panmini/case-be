package com.thy.route.repository.location;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements CustomLocationRepository {
    private final DSLContext dsl;
}
