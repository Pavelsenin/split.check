package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.List;

public interface UserDao {
    /**
     * Get user by id
     *
     * @param id
     * @return Optional<UserEntity>
     */
    UserEntity getUserById(Long userId);

    /**
     * Get users by ids
     *
     * @param ids
     * @return List of users
     */
    List<UserEntity> getUsersByIds(List<Long> userIds);


    /**
     * Save user. If user id is blank, creates new
     *
     * @param entity
     */
    void saveUser(UserEntity entity);
}
