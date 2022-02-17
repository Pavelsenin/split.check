package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.senin.pk.split.check.validation.FieldValidationError;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unified {@link org.springframework.web.bind.MissingServletRequestParameterException} handler
 */
@RestControllerAdvice
public class RestMissingServletRequestParameterExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMissingServletRequestParameterExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(MissingServletRequestParameterException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling missing request parameter exception: ", ex);
        }
        List<FieldValidationError> fieldValidationErrors = Stream.of(transformException(ex)).collect(Collectors.toList());
        return new ErrorResponse("Invalid request parameters", fieldValidationErrors);
    }

    private FieldValidationError transformException(MissingServletRequestParameterException ex) {
        return new FieldValidationError(ex.getParameterName(), "Parameter missing");
    }
}
