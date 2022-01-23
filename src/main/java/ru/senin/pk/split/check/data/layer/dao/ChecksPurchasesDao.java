package ru.senin.pk.split.check.data.layer.dao;

import java.util.List;

public interface ChecksPurchasesDao {

    /**
     * Get purchases by check id
     *
     * @param checkId
     * @return
     */
    List<Long> getPurchases(Long checkId);

    /**
     * Set purchases to check
     *
     * @param purchaseIds
     * @param checkId
     */
    void setPurchases(List<Long> purchaseIds, Long checkId);
}
