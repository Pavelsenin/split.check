package ru.senin.pk.split.check.errors;

/**
 * Thrown to indicate that user to sign up already exists
 */
public class UserAlreadyExistsException extends RuntimeException {

    private String username;

    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

    public UserAlreadyExistsException(final String message, final String username) {
        super(message);
        this.username = username;
    }

    public UserAlreadyExistsException(final Throwable cause) {
        super(cause);
    }


    public UserAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
