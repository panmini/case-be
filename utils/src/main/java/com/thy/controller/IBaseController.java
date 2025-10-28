package com.thy.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

public interface IBaseController<Entity, DTO, ID extends Serializable, USER_IDENTIFIER> {

    ResponseEntity<DTO> findById(@PathVariable("id") ID id);

    ResponseEntity<List<DTO>> findAll();

    ResponseEntity<List<DTO>> findAllById(@RequestBody List<ID> ids);

    ResponseEntity<DTO> create(@RequestBody @Valid DTO dto);

    ResponseEntity<List<DTO>> create(@RequestBody @Valid List<DTO> dtos);

    ResponseEntity<DTO> update(@PathVariable("id") ID id, @RequestBody @Valid DTO dto);

    ResponseEntity<List<DTO>> update(@RequestBody @Valid List<DTO> dtos);

    ResponseEntity<Boolean> delete(@RequestBody ID id);

    ResponseEntity<Boolean> delete(@RequestBody List<ID> ids);
}
