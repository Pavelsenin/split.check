package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.senin.pk.split.check.errors.ServiceValidationException;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;

/**
 * Unified {@link ServiceValidationException} handler
 */
@RestControllerAdvice
public class RestServiceValidationExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceValidationExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(ServiceValidationException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling service validation exception: ", ex);
        }
        return new ErrorResponse(ex.getMessage());
    }
}
