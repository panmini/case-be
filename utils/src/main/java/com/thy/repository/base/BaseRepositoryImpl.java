package com.thy.repository.base;

import com.google.common.base.Preconditions;
import com.thy.data.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseRepositoryImpl<T extends BaseEntity<ID>, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    EntityManager entityManager;

    public BaseRepositoryImpl(
            JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /* example, https://www.baeldung.com/spring-data-jpa-method-in-all-repositories */

    @Transactional
    public List<T> findByAttributeContainsText(String attributeName, String text) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
        Root<T> root = cQuery.from(getDomainClass());
        cQuery.select(root).where(builder.like(root.<String>get(attributeName), "%" + text + "%"));
        TypedQuery<T> query = entityManager.createQuery(cQuery);
        return query.getResultList();
    }

    @Override
    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    @Transactional
    @Override
    public void softDelete(ID id) {
        Preconditions.checkNotNull(id);
        Optional<T> entityOpt = findById(id);
        if (entityOpt.isPresent()) {
            T entity = entityOpt.get();
            entity.setDeleted();
            save(entity);
        } else {
            throw new EmptyResultDataAccessException(
                    String.format("No entity with id %s exists!", id), 1);
        }
    }
}
