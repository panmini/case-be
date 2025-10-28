package com.thy.repository.base;

import com.thy.data.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID> {

    @Override
    @Modifying
    @Query("update #{#entityName} e set e.deleted = true where e.id = ?1")
    void deleteById(ID id);

    @Override
    @Modifying
    @Query("update #{#entityName} e set e.deleted = true")
    void deleteAll();

    void softDelete(ID id);

    List<T> findByAttributeContainsText(String attributeName, String text);

    void flushAndClear();

    void clear();
}
