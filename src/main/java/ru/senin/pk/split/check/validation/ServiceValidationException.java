package ru.senin.pk.split.check.validation;

/**
 * Unified service validation exception
 */
public class ServiceValidationException extends RuntimeException {

    public ServiceValidationException(String message) {
        super(message);
    }
}
