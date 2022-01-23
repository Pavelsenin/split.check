package ru.senin.pk.split.check.data.layer.repositories;

import ru.senin.pk.split.check.data.layer.dto.UserDto;

public interface UserRepository {

    /**
     * Get current user
     *
     * @return
     */
    UserDto getCurrentUser();

    /**
     * Get user by id
     *
     * @param userId
     * @return
     */
    UserDto getUser(Long userId);

    /**
     * Updates user and connected entices
     *
     * @param user
     * @return
     */
    void saveUser(UserDto user);
}
