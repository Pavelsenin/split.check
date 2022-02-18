package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.senin.pk.split.check.validation.FieldValidationError;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Unified {@link MethodArgumentNotValidException} handler
 */
@RestControllerAdvice
public class RestMethodArgumentNotValidExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMethodArgumentNotValidExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(MethodArgumentNotValidException ex) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling method argument not valid exception: {}", ex.getMessage());
        }
        List<FieldValidationError> fieldValidationErrors = ex.getAllErrors().stream()
                .map(this::transformObjectError)
                .collect(Collectors.toList());
        return new ErrorResponse("Invalid request parameters", fieldValidationErrors);
    }

    private FieldValidationError transformObjectError(ObjectError error) {
        if (FieldError.class.isInstance(error)) {
            return new FieldValidationError(((FieldError) error).getField(), error.getDefaultMessage());
        } else {
            return new FieldValidationError(error.getObjectName(), error.getDefaultMessage());
        }
    }
}
