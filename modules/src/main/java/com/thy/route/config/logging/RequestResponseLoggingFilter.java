package com.thy.route.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        long timeTaken = System.currentTimeMillis() - startTime;

        String requestBody =
                sanitize(new String(wrappedRequest.getCachedBody(), request.getCharacterEncoding()));
        String responseBody =
                sanitize(
                        new String(wrappedResponse.getContentAsByteArray(), response.getCharacterEncoding()));

        log.debug(
                """
                        REQUEST-RESPONSE LOG
                        --------------------------------------------------
                        Request URI: {} {}
                        Request Body: {}
                        Response Status: {}
                        Response Body: {}
                        Time Taken: {} ms
                        --------------------------------------------------
                        """,
                request.getMethod(),
                request.getRequestURI(),
                requestBody,
                response.getStatus(),
                responseBody,
                timeTaken);

        wrappedResponse.copyBodyToResponse();
    }

    private String sanitize(String content) {
        return content.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"****\"");
    }
}
