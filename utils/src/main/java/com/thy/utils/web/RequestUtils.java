package com.thy.utils.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.UUID;

public class RequestUtils {
    private RequestUtils() {
    }

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static final String[] WHITE_LIST_URL = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
    };

    public static boolean isUriInWhitelist(String uri) {
        return Arrays.stream(WHITE_LIST_URL).anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    public static String extractDomainFromUri(String uri) {
        String[] parts = uri.split("/");

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("workplace")
                    || parts[i].equalsIgnoreCase("workspace")
                    || parts[i].equalsIgnoreCase("team")) {
                if ((i + 1 < parts.length) && !parts[i + 1].isEmpty() && isValidUUID(parts[i + 1])) {
                    return parts[i + 1];
                } else return parts[i];
            }
        }
        return null;
    }


    public static boolean isValidUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
