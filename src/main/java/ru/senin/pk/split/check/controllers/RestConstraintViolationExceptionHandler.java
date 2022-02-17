package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.validation.FieldValidationError;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unified {@link ConstraintViolationException} handler
 */
@RestControllerAdvice
public class RestConstraintViolationExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestConstraintViolationExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(ConstraintViolationException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling constraint validation exception: ", ex);
        }
        List<FieldValidationError> fieldValidationErrors = ex.getConstraintViolations().stream()
                .map(this::transformViolation)
                .collect(Collectors.toList());
        return new ErrorResponse("Invalid request parameters", fieldValidationErrors);
    }

    private FieldValidationError transformViolation(ConstraintViolation violation) {
        return new FieldValidationError(violation.getPropertyPath().toString(), violation.getMessage());
    }
}
