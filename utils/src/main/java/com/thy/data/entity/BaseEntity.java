package com.thy.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class BaseEntity<I extends Serializable> implements Persistable<I> {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private I id;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = Boolean.FALSE;

    public void setDeleted() {
        setDeleted(Boolean.TRUE);
    }

    @Transient
    protected boolean isNew = false;

}
