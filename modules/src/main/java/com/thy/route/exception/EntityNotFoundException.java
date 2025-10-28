package com.thy.route.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entity, String id) {
        super(entity + " with ID " + id + " not found");
    }

    public EntityNotFoundException(String entity, String field, String value) {
        super(entity + " with " + field + " '" + value + "' not found");
    }
}


