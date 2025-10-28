package com.thy.route.repository.transportation;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransportationRepositoryImpl implements CustomTransportationRepository {
    private final DSLContext dsl;
}
