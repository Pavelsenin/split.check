package ru.senin.pk.split.check.controllers.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Check transfer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {

    /**
     * Source user
     */
    private Long payerUser;

    /**
     * Destination user
     */
    private Long consumerUser;

    /**
     * Transfer amount
     */
    private BigDecimal amount;
}
