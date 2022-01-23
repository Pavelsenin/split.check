package ru.senin.pk.split.check.data.layer.dao;

import java.util.List;

public interface UserChecksDao {

    /**
     * Get users by check id
     *
     * @param checkId
     * @return
     */
    List<Long> getUsers(Long checkId);

    /**
     * Get checks by user id
     *
     * @param userId
     * @return
     */
    List<Long> getChecks(Long userId);

    /**
     * Set checks for user
     *
     * @param checkIds
     * @param userId
     */
    void setChecks(List<Long> checkIds, Long userId);

    /**
     * Set users for check
     *
     * @param userIds
     * @param checkId
     */
    void setUsers(List<Long> userIds, Long checkId);
}
