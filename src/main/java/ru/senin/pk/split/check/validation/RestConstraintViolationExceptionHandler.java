package ru.senin.pk.split.check.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ValidationErrorResponse handleException(ConstraintViolationException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling constraint validation exception: ", ex);
        }
        List<FieldValidationError> fieldValidationErrors = ex.getConstraintViolations().stream()
                .map(this::transformViolation)
                .collect(Collectors.toList());
        return new ValidationErrorResponse("Invalid request parameters", fieldValidationErrors);
    }

    private FieldValidationError transformViolation(ConstraintViolation violation) {
        return new FieldValidationError(violation.getPropertyPath().toString(), violation.getMessage());
    }
}
