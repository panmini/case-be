package com.thy.controller;

import com.google.common.base.Preconditions;
import com.thy.data.dto.BaseDTO;
import com.thy.data.entity.BaseEntity;
import com.thy.service.IBaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseController<E extends BaseEntity, D extends BaseDTO, I extends Serializable, U>
        implements IBaseController<E, D, I, U> {

    @Autowired
    protected IBaseService<E, D, I> baseService;

    @Autowired
    protected HttpServletRequest httpServletRequest;

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<D> findById(@PathVariable("id") I id) {
        return ResponseEntity.ok().body(baseService.findById(id).orElseThrow());
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> findAll() {
        return ResponseEntity.ok().body(baseService.findAll());
    }

    @Override
    @PostMapping(value = "findAllById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> findAllById(@RequestBody List<I> ids) {
        return ResponseEntity.ok().body(baseService.findAllById(ids));
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<D> create(@RequestBody @Valid D dto) {
        Preconditions.checkNotNull(dto);
        D saved = baseService.save(dto);
        baseService.flushAndClear();
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    @PostMapping(value = "batch_create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> create(@RequestBody @Valid List<D> dtos) {
        Preconditions.checkNotNull(dtos);
        List<D> newDTOs = new ArrayList<>();
        for (D dto : dtos) {
            newDTOs.add(baseService.save(dto));
        }
        baseService.flushAndClear();
        return ResponseEntity.status(HttpStatus.CREATED).body(newDTOs);
    }

    @Override
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<D> update(@PathVariable("id") I id, @RequestBody @Valid D dto) {
        Preconditions.checkNotNull(dto);

        D updated = baseService.update(dto);
        Optional<D> lastDTO = baseService.findById(id);
        if (lastDTO.isPresent()) {
            updated = lastDTO.get();
        }
        baseService.flushAndClear();
        return ResponseEntity.ok().body(updated);
    }

    @Override
    @PutMapping(value = "batch_update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> update(@RequestBody @Valid List<D> dtos) {
        Preconditions.checkNotNull(dtos);
        List<D> updatedDTOs = new ArrayList<>();
        for (D dto : dtos) {
            updatedDTOs.add(baseService.update(dto));
        }
        baseService.flushAndClear();
        return ResponseEntity.status(HttpStatus.OK).body(updatedDTOs);
    }

    @Override
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@RequestBody I id) {
        baseService.deleteById(id);
        baseService.flushAndClear();
        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }

    @Override
    @DeleteMapping(value = "batch_delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@RequestBody List<I> ids) {

        for (I id : ids) {
            baseService.deleteById(id);
        }
        baseService.flushAndClear();
        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }
}
