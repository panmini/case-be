package com.thy.exception;

import com.thy.data.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class BaseExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(BaseException ex) {
        logger.error("Base exception in app:\n{}: {}", ex.getCode(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        String errorMessage = String.join("; ", errors);
        logger.warn("Validation error: {}", errorMessage);

        ErrorResponse response = new ErrorResponse("VALIDATION_ERROR", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        String errorMessage = String.join("; ", errors);
        logger.warn("Constraint violation: {}", errorMessage);

        ErrorResponse response = new ErrorResponse("VALIDATION_ERROR", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Data integrity violation";
        if (ex.getRootCause() != null) {
            message = ex.getRootCause().getMessage();
            // Extract meaningful parts from SQL exception
            if (message.contains("unique constraint")) {
                message = "Data already exists with the same unique values";
            } else if (message.contains("foreign key constraint")) {
                message = "Referenced data does not exist";
            }
        }

        logger.warn("Data integrity violation: {}", message);

        ErrorResponse response = new ErrorResponse("DATA_INTEGRITY_ERROR", message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        logger.warn("Entity not found: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse("INVALID_INPUT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.warn("Invalid request body: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse("INVALID_REQUEST_BODY", "Invalid JSON format in request body");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = "Required parameter '" + ex.getParameterName() + "' is missing";
        logger.warn("Missing parameter: {}", message);

        ErrorResponse response = new ErrorResponse("MISSING_PARAMETER", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Filter stack trace to show only application-related traces
        String formattedTrace = getFilteredStackTrace(ex);

        logger.error("Unhandled exception in app:\n{}", formattedTrace, ex);

        ErrorResponse response = new ErrorResponse("INTERNAL_ERROR", "Something went wrong. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String getFilteredStackTrace(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();

        if (stackTrace.length == 0) {
            return "No stack trace available";
        }

        return Arrays.stream(stackTrace)
                .filter(el -> el.getClassName().startsWith("com.thy")) // Filter to your application packages
                .limit(10) // Limit to first 10 application traces
                .map(el -> String.format("at %s.%s(%s:%d)",
                        el.getClassName(),
                        el.getMethodName(),
                        el.getFileName(),
                        el.getLineNumber()))
                .collect(Collectors.joining("\n"));
    }
}