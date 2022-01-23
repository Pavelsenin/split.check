package ru.senin.pk.split.check.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNewPurchaseRequest {
    /**
     * Purchase name
     */
    private String name;

    /**
     * Purchase name
     */
    private BigDecimal cost;

    /**
     * Purchase payer id
     */
    private Long payer;

    /**
     * Purchase consumers ids
     */
    private List<Long> consumers;
}
