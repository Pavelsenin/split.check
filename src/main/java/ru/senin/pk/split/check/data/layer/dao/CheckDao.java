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
    Optional<CheckEntity> getCheck(Long id);

    /**
     * Get checks by ids
     *
     * @param ids
     * @return List of checks
     */
    List<CheckEntity> getChecks(List<Long> ids);
}
