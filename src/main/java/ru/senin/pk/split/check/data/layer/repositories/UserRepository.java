package ru.senin.pk.split.check.data.layer.repositories;

import ru.senin.pk.split.check.data.layer.dto.UserDto;

public interface UserRepository {
    /**
     * Get user by id
     *
     * @param userId
     * @return
     */
    UserDto getUser(Long userId);
}
