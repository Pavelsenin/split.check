package ru.senin.pk.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PurchaseEntity {
    /**
     * Purchase id
     */
    private Long id;

    /**
     * Purchase name
     */
    private String name;

    /**
     * Purchase cost
     */
    private BigDecimal cost;

    /**
     * Purchase payer
     */
    private UserEntity payer;

    /**
     * Purchase consumers
     */
    private List<UserEntity> consumers;
}
