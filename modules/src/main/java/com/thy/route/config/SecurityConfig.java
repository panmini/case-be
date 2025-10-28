package com.thy.route.config;

import com.thy.route.config.logging.RequestResponseLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RequestResponseLoggingFilter requestResponseLoggingFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().permitAll())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(corsFilter(), CorsFilter.class);
        http.addFilterBefore(requestResponseLoggingFilter, CorsFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOrigin("http://localhost:3000");
        corsConfig.addAllowedOrigin("http://localhost:8080");

        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");

        corsConfig.addAllowedHeader("Content-Type");
        corsConfig.addAllowedHeader("Authorization");
        corsConfig.addAllowedHeader("Accept");
        corsConfig.addAllowedHeader("Accept-Encoding");
        corsConfig.addAllowedHeader("Accept-Language");
        corsConfig.addAllowedHeader("X-Requested-With");
        corsConfig.addAllowedHeader("Cookie");
        corsConfig.addAllowedHeader("Access-Control-Allow-Credentials");
        corsConfig.addAllowedHeader("Connection");
        corsConfig.addAllowedHeader("Content-Length");
        corsConfig.addAllowedHeader("Host");
        corsConfig.addAllowedHeader("Origin");
        corsConfig.addAllowedHeader("Referer");
        corsConfig.addAllowedHeader("Sec-Ch-Ua");
        corsConfig.addAllowedHeader("User-Agent");

        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }
}