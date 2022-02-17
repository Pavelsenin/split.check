package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Unified unexpected exception handler
 */
@RestControllerAdvice
public class RestUnexpectedExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUnexpectedExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        LOGGER.error("Unexpected error: ", ex);
    }
}
