package com.thy.route.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI caseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Case Routes API")
                        .description("API for managing locations, transportations and finding routes")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("CASE Routes Team")
                        ));
    }
}