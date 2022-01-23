package ru.senin.pk.split.check.data.layer.dao;

import java.util.List;

public interface UserChecksDao {

    /**
     * Links checks to user
     *
     * @param checkIds
     * @param userId
     */
    void linkChecksToUser(List<Long> checkIds, Long userId);

    /**
     * Unlinks all checks from user
     *
     * @param userId
     */
    void unlinkChecksFromUser(Long userId);
}
