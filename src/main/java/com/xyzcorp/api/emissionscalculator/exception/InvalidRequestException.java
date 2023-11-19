package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ApiException {

    public InvalidRequestException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
