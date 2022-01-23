package ru.senin.pk.split.check.data.layer.dao;

import java.util.List;

public interface ChecksPurchasesDao {

    /**
     * Links purchases to check
     *
     * @param purchaseIds
     * @param checkId
     */
    void linkPurchasesToCheck(List<Long> purchaseIds, Long checkId);

    /**
     * Unlinks all purchases from check
     *
     * @param checkId
     */
    void unlinkPurchasesFromCheck(Long checkId);
}
