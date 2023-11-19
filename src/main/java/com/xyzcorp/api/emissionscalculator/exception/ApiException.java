package com.xyzcorp.api.emissionscalculator.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final String contract;
    private final HttpStatus status;

    public ApiException(String message, String contract, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.contract = contract;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getContract() {
        return contract;
    }

    public HttpStatus getStatus() {
        return status;
    }

}