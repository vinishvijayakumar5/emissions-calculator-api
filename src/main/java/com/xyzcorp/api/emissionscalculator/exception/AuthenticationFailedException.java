package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends ApiException {

    public AuthenticationFailedException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
