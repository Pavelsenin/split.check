package ru.senin.pk.split.check.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Unified {@link HttpMessageNotReadableException} handler
 */
@RestControllerAdvice
public class RestHttpMessageNotReadableExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestHttpMessageNotReadableExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleException(HttpMessageNotReadableException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling http message not readable exception: ", ex);
        }
        return new ValidationErrorResponse("Invalid request parameters");
    }
}
