package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.UserAuthEntity;

public interface UserAuthDao {
    /**
     * Get auth info by username
     *
     * @param id
     * @return Optional<UserEntity>
     */
    UserAuthEntity getUserAuthByUsername(String username);


    /**
     * Save user auth info. If user id is blank, creates new
     *
     * @param entity
     */
    void saveUserAuth(UserAuthEntity entity);
}
