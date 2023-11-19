package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class CompanyNotFoundException extends ApiException {

    public CompanyNotFoundException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
