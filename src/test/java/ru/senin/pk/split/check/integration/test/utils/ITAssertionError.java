package ru.senin.pk.split.check.integration.test.utils;

/**
 * Exception thrown then integration test step result assert failed
 */
public class ITAssertionError extends RuntimeException {

    public ITAssertionError() {
        super();
    }

    public ITAssertionError(final String message) {
        super(message);
    }
}
