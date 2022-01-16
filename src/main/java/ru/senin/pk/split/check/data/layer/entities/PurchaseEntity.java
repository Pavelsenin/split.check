package ru.senin.pk.split.check.data.layer.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
     * Purchase payer id
     */
    private Long payerId;

    /**
     * Purchase consumers ids
     */
    private List<Long> consumerIds;
}
