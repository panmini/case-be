package com.thy.service;

import com.thy.data.dto.BaseDTO;
import com.thy.data.entity.BaseEntity;
import com.thy.repository.base.BaseRepository;
import com.thy.repository.mapper.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseService<E extends BaseEntity<I>, D extends BaseDTO, I extends Serializable>
        implements IBaseService<E, D, I> {

    @Autowired
    protected BaseRepository<E, I> dao;
    @Autowired
    protected BaseMapper<E, D> mapper;

    @Override
    @Transactional
    public D save(D dto) {

        E entity = mapper.toEntity(dto);
        entity.setNew(true);
        entity = dao.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public List<D> save(List<D> dtos) {
        List<E> entities = new ArrayList<>();
        Optional.ofNullable(dtos)
                .orElse(new ArrayList<>())
                .forEach(
                        dto -> {
                            entities.add(mapper.toEntity(dto));
                        });

        List<E> savedEntities = dao.saveAll(entities);
        dao.flushAndClear();
        return mapper.toDTOs(savedEntities);
    }

    @Override
    @Transactional
    public D update(D dto) {

        E entity = mapper.toEntity(dto);
        entity.setNew(false);
        entity = dao.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public void delete(D dto) {
        E entity = mapper.toEntity(dto);
        entity.setDeleted(true);
        dao.softDelete(entity.getId());
    }

    @Override
    public void flushAndClear() {
        dao.flushAndClear();
    }

    @Override
    public Optional<D> findById(I id) {
        Optional<E> optional = dao.findById(id);
        return optional.map(entity -> mapper.toDTO(entity));
    }

    @Override
    public List<D> findAll() {
        List<E> entityList = dao.findAll();
        return mapper.toDTOs(entityList);
    }

    @Override
    public void deleteById(I id) {
        dao.deleteById(id);
    }

    @Override
    public List<D> findAllById(List<I> ids) {
        List<E> entityList = dao.findAllById(ids);
        return mapper.toDTOs(entityList);
    }
}
