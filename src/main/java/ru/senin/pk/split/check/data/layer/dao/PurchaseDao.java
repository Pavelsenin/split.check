package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface PurchaseDao {
    /**
     * Get purchase by id
     *
     * @param id
     * @return Optional<PurchaseEntity>
     */
    Optional<PurchaseEntity> getPurchaseById(Long purchaseId);

    /**
     * Get purchases by ids
     *
     * @param ids
     * @return List of purchases
     */
    List<PurchaseEntity> getPurchasesByIds(List<Long> purchaseIds);

    /**
     * Get purchases by check id
     *
     * @param checkId
     * @return List of purchases
     */
    List<PurchaseEntity> getPurchasesByCheckId(Long checkId);

    /**
     * Save purchase. If entity id is blank, creates new
     *
     * @param entity
     */
    void savePurchase(PurchaseEntity entity);
}
