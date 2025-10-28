package com.thy.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.details = new ArrayList<>();
    }

    public ErrorResponse(String code, String message, List<String> details) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.details = details != null ? details : new ArrayList<>();
    }

    public ErrorResponse withDetail(String detail) {
        this.details.add(detail);
        return this;
    }
}