package ru.senin.pk.split.check.errors;

/**
 * Thrown to indicate that something with db went wrong
 */
public class DataBaseException extends RuntimeException {

    public DataBaseException() {
        super();
    }

    public DataBaseException(final String message) {
        super(message);
    }

    public DataBaseException(final Throwable cause) {
        super(cause);
    }


    public DataBaseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
