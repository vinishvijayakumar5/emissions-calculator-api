package com.xyzcorp.api.emissionscalculator.dto;

public record GenericResponseDto (
        boolean success,
        String message,
        String code
) { }
