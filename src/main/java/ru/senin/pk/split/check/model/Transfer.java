package ru.senin.pk.split.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Transfer of money between payer user and consumer user as part of a check
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    private User payer;

    private User consumer;

    private BigDecimal amount;
}
