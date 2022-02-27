package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.FriendsRequestEntity;

import java.util.List;

public interface FriendsRequestsDao {

    /**
     * Receive list of friends request and statuses by user id
     *
     * @param userId
     * @return
     */
    List<FriendsRequestEntity> getFriendsRequests(Long userId);

    /**
     * Save friend requests
     *
     * @param userId
     * @param friendRequests
     */
    void saveFriendsRequests(Long userId, List<FriendsRequestEntity> friendRequests);
}
