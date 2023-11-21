package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class AuthTokenNotFoundException extends ApiException {

    public AuthTokenNotFoundException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
