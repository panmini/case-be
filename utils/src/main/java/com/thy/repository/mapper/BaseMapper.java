package com.thy.repository.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper<E, DTO> extends Serializable {

    default List<DTO> toDTOs(List<E> entityList) {
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    DTO toDTO(E entity);

    default List<E> toEntities(List<DTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    E toEntity(DTO dto);
}
