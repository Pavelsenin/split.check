package ru.senin.pk.split.check.data.layer.dao;

import java.util.List;

public interface PurchasesConsumersDao {

    /**
     * Receive consumer users from purchase
     *
     * @param purchaseId
     * @return
     */
    List<Long> getConsumerUsers(Long purchaseId);

    /**
     * Set consumer users to purchase
     *
     * @param userIds
     * @param purchaseId
     */
    void setConsumerUsers(List<Long> userIds, Long purchaseId);
}
