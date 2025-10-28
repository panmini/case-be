package com.thy.controller;

import com.google.common.base.Preconditions;
import com.thy.service.IBaseCacheableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
public class BaseCacheableController<Entity, DTO, ID extends Serializable, USER_IDENTIFIER>
        implements IBaseCacheableController<Entity, DTO, ID, USER_IDENTIFIER> {

    @NonNull
    protected String cacheKey;

    @Autowired(required = false)
    protected IBaseCacheableService<Entity, DTO, ID, USER_IDENTIFIER> baseCacheableService;

    protected final HttpServletRequest httpServletRequest;

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DTO> findById(@PathVariable("id") ID id) {
        DTO dto = Preconditions.checkNotNull(baseCacheableService.findById(id, cacheKey).orElseThrow());
        return ResponseEntity.ok().body(Preconditions.checkNotNull(dto));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DTO>> findAll() {
        return ResponseEntity.ok().body(baseCacheableService.findAll(cacheKey));
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DTO> create(@RequestBody @Valid DTO dto) {
        Preconditions.checkNotNull(dto);
        DTO saved = baseCacheableService.save(dto, cacheKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DTO> update(@PathVariable("id") ID id, @RequestBody @Valid DTO dto) {
        Preconditions.checkNotNull(dto);
        DTO updated = baseCacheableService.update(dto, cacheKey);
        return ResponseEntity.ok().body(updated);
    }

    @Override
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") ID id) {
        baseCacheableService.delete(id, cacheKey);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }
}
