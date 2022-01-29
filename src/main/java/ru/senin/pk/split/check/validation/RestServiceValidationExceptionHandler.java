package ru.senin.pk.split.check.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Unified {@link ServiceValidationException} handler
 */
@RestControllerAdvice
public class RestServiceValidationExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceValidationExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleException(ServiceValidationException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling service validation exception: ", ex);
        }
        return new ValidationErrorResponse(ex.getMessage());
    }
}
