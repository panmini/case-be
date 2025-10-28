package com.thy.exception;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class BaseException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(BaseException.class);
    private final String code;

    public BaseException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;

        // Log the exception with stack trace
        logger.error("Exception occurred - Code: {}, Message: {}", code, message, cause);
    }
}
