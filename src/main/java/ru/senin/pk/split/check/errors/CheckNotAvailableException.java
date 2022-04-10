package ru.senin.pk.split.check.errors;

import lombok.Getter;

/**
 * Thrown to indicate that check with gived id not exists or not available for current user
 */
public class CheckNotAvailableException extends RuntimeException {

    @Getter
    private Long checkId;

    public CheckNotAvailableException(final String message, final Long checkId) {
        super(message);
        this.checkId = checkId;
    }
}
