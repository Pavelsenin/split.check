package ru.senin.pk.split.check.errors;

import lombok.Getter;

/**
 * Thrown to indicate that user to sign up already exists
 */
public class UserAlreadyExistsException extends RuntimeException {

    @Getter
    private String username;

    public UserAlreadyExistsException(final String message, final String username) {
        super(message);
        this.username = username;
    }
}
