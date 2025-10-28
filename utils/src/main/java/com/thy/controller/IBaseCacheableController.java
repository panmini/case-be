package com.thy.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

public interface IBaseCacheableController<Entity, DTO, ID extends Serializable, USER_IDENTIFIER> {

    ResponseEntity<DTO> findById(@PathVariable("id") ID id);

    ResponseEntity<List<DTO>> findAll();

    ResponseEntity<DTO> create(@RequestBody @Valid DTO dto);

    ResponseEntity<DTO> update(@PathVariable("id") ID id, @RequestBody @Valid DTO dto);

    ResponseEntity<Boolean> delete(@PathVariable("id") ID id);
}
