package ru.senin.pk.split.check.data.layer.repositories;

import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.User;

public interface UserRepository {

    /**
     * Get current user by username
     *
     * @param username
     * @return CurrentUser or null if not found
     */
    CurrentUser getCurrentUserByUsername(String username);

    /**
     * Updates current user and connected entices
     *
     * @param user
     * @return
     */
    void saveCurrentUser(CurrentUser user);

    /**
     * Get user by id
     *
     * @param userId
     * @return
     */
    User getUser(Long userId);
}
