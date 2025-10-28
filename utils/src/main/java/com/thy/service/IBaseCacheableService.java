package com.thy.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IBaseCacheableService<Entity, DTO, ID extends Serializable, USER_IDENTIFIER> {

    DTO save(DTO dto, String cacheKey);

    DTO update(DTO dto, String cacheKey);

    void delete(ID id, String cacheKey);

    void delete(DTO dto, String cacheKey);

    Optional<DTO> findById(ID id, String cacheKey);

    List<DTO> findAll(String cacheKey);
}
