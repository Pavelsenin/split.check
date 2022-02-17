package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;


/**
 * Unified {@link HttpMessageNotReadableException} handler
 */
@RestControllerAdvice
public class RestHttpMessageNotReadableExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestHttpMessageNotReadableExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(HttpMessageNotReadableException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling http message not readable exception: ", ex);
        }
        return new ErrorResponse("Invalid request parameters");
    }
}
