package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class ClaimsParsingException extends ApiException {

    public ClaimsParsingException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
