package ru.senin.pk.split.check.data.layer.repositories;

import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.User;

public interface UserRepository {

    /**
     * Get current user by username
     *
     * @param username
     * @return
     */
    CurrentUser getCurrentUserByUsername(String username);

    /**
     * Get current user
     *
     * @return
     */
    CurrentUser getCurrentUser();

    /**
     * Get current user by id
     *
     * @param id
     * @return
     */
    CurrentUser getCurrentUserById(Long id);

    /**
     * Get user by id
     *
     * @param userId
     * @return
     */
    User getUser(Long userId);

    /**
     * Updates current user and connected entices
     *
     * @param user
     * @return
     */
    void saveCurrentUser(CurrentUser user);
}
