package com.xyzcorp.api.emissionscalculator.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiConfig {

    private static final String TITLE = "Emissions Calculator API";
    private static final String DESCRIPTION = "APIs supported for emissions calculator";
    private static final String BEARER_AUTH = "BearerAuth";
    private static final String BEARER = "bearer";
    private static final String BEARER_FORMAT = "JWT";

    @Value("${application.version}")
    private String apiVersion;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title(TITLE)
                .description(DESCRIPTION)
                .version(apiVersion))
                .components(new Components().addSecuritySchemes(BEARER_AUTH, getSecurityScheme()))
                .security(Collections.singletonList(new SecurityRequirement().addList(BEARER_AUTH)));
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(BEARER)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat(BEARER_FORMAT);
    }
}
