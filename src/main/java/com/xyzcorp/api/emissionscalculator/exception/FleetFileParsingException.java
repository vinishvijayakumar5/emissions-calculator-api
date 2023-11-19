package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class FleetFileParsingException extends ApiException {

    public FleetFileParsingException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
