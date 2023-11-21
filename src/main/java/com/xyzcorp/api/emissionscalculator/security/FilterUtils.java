package com.xyzcorp.api.emissionscalculator.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface FilterUtils {

    default boolean isPrivateRequest(HttpServletRequest request) {
        return request.getRequestURL().indexOf("/api/private/") > 0;
    }
}
