package com.xyzcorp.api.emissionscalculator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.UserDto;
import com.xyzcorp.api.emissionscalculator.service.UserPublicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Configuration
@Slf4j
public class AdminConfig {

    private static final String FILE_LOC = "classpath:admin.json";

    @Autowired
    private UserPublicService userPublicService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public void register() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getResource().getInputStream();
            userPublicService.register(objectMapper.readValue(inputStream, UserDto.class));
        } catch (Exception e) {
            log.error("Error occurred while registering default admin user.", e);
        } finally {
            if(Objects.nonNull(inputStream)) {
                inputStream.close();
            }
        }
    }

    private Resource getResource() {
        return resourceLoader.getResource(FILE_LOC);
    }
}
