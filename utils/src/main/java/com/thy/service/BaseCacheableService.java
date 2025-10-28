package com.thy.service;

import com.thy.data.dto.BaseDTO;
import com.thy.data.entity.BaseEntity;
import com.thy.repository.base.BaseRepository;
import com.thy.repository.mapper.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BaseCacheableService<
        Entity extends BaseEntity<ID>,
        DTO extends BaseDTO,
        ID extends Serializable,
        USER_IDENTIFIER>
        implements IBaseCacheableService<Entity, DTO, ID, USER_IDENTIFIER> {

    protected BaseRepository<Entity, ID> dao;

    protected BaseMapper<Entity, DTO> mapper;

    @Override
    @Transactional
    // @formatter:off
  @Caching(
      evict = {
        @CacheEvict(value = "contantsa ekle", key = "{#cacheKey, #dto.id}"),
        @CacheEvict(value = "constant ekle", key = "#cacheKey")
      })
  // @formatter:on
    public DTO save(DTO dto, String cacheKey) {

        setIdIfNotExists(dto);
        Entity entity = mapper.toEntity(dto);
        entity.setNew(true);
        entity = dao.save(entity);
        dao.flushAndClear();
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    // @formatter:off
  @Caching(
      evict = {
        @CacheEvict(value = "cache ekle", key = "{#cacheKey, #dto.id}"),
        @CacheEvict(value = "constant ekle", key = "#cacheKey")
      })
  // @formatter:on
    public DTO update(DTO dto, String cacheKey) {

        Entity entity = mapper.toEntity(dto);
        entity.setNew(false);
        entity = dao.save(entity);
        dao.flushAndClear();
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    // @formatter:off
  @Caching(
      evict = {
        @CacheEvict(value = "constant ekle", key = "{#cacheKey, #id}"),
        @CacheEvict(value = "constant ekle", key = "#cacheKey")
      })
  // @formatter:on
    public void delete(ID id, String cacheKey) {
        dao.deleteById(id);
        dao.flushAndClear();
    }

    @Override
    @Transactional
    // @formatter:off
  @Caching(
      evict = {
        @CacheEvict(value = "", key = "{#cacheKey, #dto.id}"),
        @CacheEvict(value = "", key = "#cacheKey")
      })
  // @formatter:on
    public void delete(DTO dto, String cacheKey) {
        Entity entity = mapper.toEntity(dto);
        entity.setNew(false);
        dao.softDelete(entity.getId());
        dao.flushAndClear();
    }

    @Override
    @Cacheable(value = "", key = "{#cacheKey, #id.toString()}")
    public Optional<DTO> findById(ID id, String cacheKey) {
        Optional<Entity> optional = dao.findById(id);
        return optional.map(entity -> mapper.toDTO(entity));
    }

    @Override
    @Cacheable(value = "", key = "#cacheKey", unless = "#result==null or #result.size()==0")
    public List<DTO> findAll(String cacheKey) {
        return dao.findAll().stream().map(entity -> mapper.toDTO(entity)).collect(Collectors.toList());
    }

    protected void setIdIfNotExists(DTO dto) {
        if (Objects.isNull(dto.getId())) {
            dto.setId(UUID.randomUUID());
        }
    }
}
