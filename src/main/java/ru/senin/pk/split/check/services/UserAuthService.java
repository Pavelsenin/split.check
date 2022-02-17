package ru.senin.pk.split.check.services;

import ru.senin.pk.split.check.errors.UserAlreadyExistsException;
import ru.senin.pk.split.check.model.CurrentUser;

public interface UserAuthService {

    /**
     * Signs up new user
     *
     * @param user
     * @return new user
     * @throws UserAlreadyExistsException if user with username already exists
     */
    CurrentUser signUpUser(CurrentUser user);

    /**
     * Returns current user bounded with session
     *
     * @return CurrentUser or null if session not bound
     */
    CurrentUser getCurrentUser();
}
