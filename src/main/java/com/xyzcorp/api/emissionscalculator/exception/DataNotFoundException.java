package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends ApiException {

    public DataNotFoundException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
