package ru.senin.pk.split.check.data.layer.dao;

import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;

import java.util.List;
import java.util.Optional;

public interface PurchasesPayersDao {

    /**
     * Receive payer user from purchase
     *
     * @param purchaseId
     * @return
     */
    Long getPayerUser(Long purchaseId);

    /**
     * Set payer user to purchase
     *
     * @param userId
     * @param purchaseId
     */
    void setPayerUser(Long userId, Long purchaseId);
}
