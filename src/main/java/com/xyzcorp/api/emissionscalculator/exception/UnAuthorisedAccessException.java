package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorisedAccessException extends ApiException {

    public UnAuthorisedAccessException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
