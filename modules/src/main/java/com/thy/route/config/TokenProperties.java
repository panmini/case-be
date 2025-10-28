package com.thy.route.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "thy.auth.token")
@Getter
@Setter
public class TokenProperties {
    private Long passwordResetDurationMs = 900000L;
    private Long refreshDurationMs = 36000000L;
} 