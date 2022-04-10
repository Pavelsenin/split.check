package ru.senin.pk.split.check.errors;

import lombok.Getter;

import java.util.List;


/**
 * Thrown to indicate attempt ща interaction with a non-friend user
 */
public class UnknownUserException extends RuntimeException {

    @Getter
    private List<Long> unknownUserIds;

    public UnknownUserException(String message, List<Long> unknownUserIds) {
        super(message);
        this.unknownUserIds = unknownUserIds;
    }
}
