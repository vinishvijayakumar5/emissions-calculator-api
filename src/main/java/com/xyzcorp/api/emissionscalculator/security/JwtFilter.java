package com.xyzcorp.api.emissionscalculator.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import com.xyzcorp.api.emissionscalculator.exception.ApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter implements FilterUtils {

    @Autowired
    private JwtManager jwtManager;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       if(isPrivateRequest(request)) {
           try {
               jwtManager.validateToken(request);
           } catch (ApiException e) {
               response.setStatus(HttpStatus.UNAUTHORIZED.value());
               response.getWriter().write(unAuthResponse(e));
               return;
           }
       }
       filterChain.doFilter(request, response);
    }

    private String unAuthResponse(ApiException e) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new GenericResponseDto(false, e.getMessage(), e.getContract()));
    }

}
