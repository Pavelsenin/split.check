package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.Optional;

public interface UserDao {
    /**
     * Get user by id
     * @param id
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> getUser(Long id);
}
