package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class UserExistsException extends ApiException {

    public UserExistsException(String message, String contract, HttpStatus status) {
        super(message, contract, status);
    }
}
