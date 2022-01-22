package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.CheckEntity;

import java.util.List;
import java.util.Optional;

public interface CheckDao {
    /**
     * Get check by id
     *
     * @param id
     * @return Optional<CheckEntity>
     */
    Optional<CheckEntity> getCheckById(Long checkId);

    /**
     * Get checks by ids
     *
     * @param ids
     * @return List of checks
     */
    List<CheckEntity> getChecksByIds(List<Long> checkIds);

    /**
     * Get checks by user id
     *
     * @param userId
     * @return List of checks
     */
    List<CheckEntity> getChecksByUserId(Long userId);

    /**
     * Save check. If entity id is blank, creates new
     *
     * @param entity
     */
    void saveCheck(CheckEntity entity);

}
