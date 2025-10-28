package com.thy.utils.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thy.data.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorResponseUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void createHttpServletErrorResponse(
            HttpServletResponse response, String errorCode, String message, int httpStatus)
            throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("application/json");

        ErrorResponse error = new ErrorResponse(errorCode, message);
        String body = objectMapper.writeValueAsString(error);

        response.getWriter().write(body);
    }
}
