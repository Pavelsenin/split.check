package ru.senin.pk.split.check.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPurchasesResponse {
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
    private Long payer;

    /**
     * Purchase consumers
     */
    private List<Long> consumers;
}
