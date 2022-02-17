package ru.senin.pk.split.check.errors;

/**
 * Unified service validation exception
 */
public class ServiceValidationException extends RuntimeException {

    public ServiceValidationException(String message) {
        super(message);
    }
}
