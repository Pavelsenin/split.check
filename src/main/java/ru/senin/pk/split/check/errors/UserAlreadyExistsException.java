package ru.senin.pk.split.check.errors;

/**
 * Thrown to indicate that user to sign up already exists
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

    public UserAlreadyExistsException(final Throwable cause) {
        super(cause);
    }


    public UserAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
