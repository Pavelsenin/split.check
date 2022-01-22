package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    /**
     * Get user by id
     *
     * @param id
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> getUserById(Long userId);

    /**
     * Get users by ids
     *
     * @param ids
     * @return List of users
     */
    List<UserEntity> getUsersByIds(List<Long> userIds);

    /**
     * Get users by purchase id
     *
     * @param purchaseId
     * @return List of users
     */
    List<UserEntity> getUsersByCheckId(Long checkId);

    /**
     * Get payer by purchase id
     *
     * @param purchaseId
     * @return Lser
     */
    UserEntity getPayerByPurchaseId(Long purchaseId);

    /**
     * Get consumers by purchase id
     *
     * @param purchaseId
     * @return List of users
     */
    List<UserEntity> getConsumersByPurchaseId(Long purchaseId);
}
