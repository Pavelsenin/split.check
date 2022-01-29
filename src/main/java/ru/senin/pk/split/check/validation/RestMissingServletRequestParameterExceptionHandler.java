package ru.senin.pk.split.check.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ValidationErrorResponse handleException(MissingServletRequestParameterException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling missing request parameter exception: ", ex);
        }
        List<FieldValidationError> fieldValidationErrors = Stream.of(transformException(ex)).collect(Collectors.toList());
        return new ValidationErrorResponse("Invalid request parameters", fieldValidationErrors);
    }

    private FieldValidationError transformException(MissingServletRequestParameterException ex) {
        return new FieldValidationError(ex.getParameterName(), "Parameter missing");
    }
}
