package com.xyzcorp.api.emissionscalculator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.UserDto;
import com.xyzcorp.api.emissionscalculator.service.UserPublicService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class AdminConfig {

    @Value("${user.profile.default}")
    private String fileLoc;

    @Autowired
    private UserPublicService userPublicService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void register() throws IOException {
        try(InputStream inputStream = getResource().getInputStream()) {
            userPublicService.register(objectMapper.readValue(inputStream, UserDto.class));
        } catch (Exception e) {
            log.error("Error occurred while registering default admin user.", e);
        }
    }

    private Resource getResource() {
        return resourceLoader.getResource(fileLoc);
    }
}
