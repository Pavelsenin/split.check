package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;

import java.util.List;
import java.util.Optional;

public interface PurchaseDao {
    /**
     * Get purchase by id
     *
     * @param id
     * @return Optional<PurchaseEntity>
     */
    Optional<PurchaseEntity> getPurchase(Long id);


    /**
     * Get purchases by ids
     *
     * @param ids
     * @return List of purchases
     */
    List<PurchaseEntity> getPurchases(List<Long> ids);
}
