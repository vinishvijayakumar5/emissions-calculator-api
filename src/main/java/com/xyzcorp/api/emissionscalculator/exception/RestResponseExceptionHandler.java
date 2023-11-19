package com.xyzcorp.api.emissionscalculator.exception;

import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Objects.nonNull;

@RestControllerAdvice
@Slf4j
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ApiException.class})
    public ResponseEntity<Object> exceptionHandler(ApiException exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return frameResponseEntity(httpServletRequest, httpServletResponse, exception);
    }

    @ExceptionHandler(value = { Exception.class})
    public ResponseEntity<Object> exceptionHandler(Exception exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return frameResponseEntity(httpServletRequest, httpServletResponse, exception);
    }

    private ResponseEntity frameResponseEntity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                               ApiException exception) {
        int status = nonNull(exception.getStatus()) ? exception.getStatus().value() : httpServletResponse.getStatus();
        ResponseEntity responseEntity = ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericResponseDto(false, exception.getMessage(), exception.getContract()));
        log(httpServletRequest.getRequestURL().toString(), responseEntity);
        return responseEntity;
    }

    private ResponseEntity frameResponseEntity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception exception) {
        logger.error("Unexpected exception", exception);
        ResponseEntity responseEntity = ResponseEntity
                .status(httpServletResponse.getStatus() == 200
                        ? HttpStatus.INTERNAL_SERVER_ERROR.value() : httpServletResponse.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GenericResponseDto(false, exception.getMessage(), "E0000"));
        log(httpServletRequest.getRequestURL().toString(), responseEntity);
        return responseEntity;
    }

    private void log(String url, ResponseEntity responseEntity) {
        logger.error(String.format("Encounter error for [%s] with entity [%s]", url, responseEntity.toString()));
    }

}