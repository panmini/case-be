package com.thy.data.dto;

import com.thy.data.Identifier;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseDTO<I extends Serializable> implements Identifier<I> {
    private I id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;
}
