package com.thy.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IBaseService<Entity, DTO, ID extends Serializable> {

    DTO save(DTO dto);

    List<DTO> save(List<DTO> dtos);

    DTO update(DTO dto);

    void delete(DTO dto);

    void flushAndClear();

    Optional<DTO> findById(ID id);

    List<DTO> findAll();

    void deleteById(ID id);

    List<DTO> findAllById(List<ID> ids);
}
