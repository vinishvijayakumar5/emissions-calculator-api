package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class CompanyExistsException extends ApiException {

    public CompanyExistsException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
