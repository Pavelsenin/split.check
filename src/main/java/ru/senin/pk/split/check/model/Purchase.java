package ru.senin.pk.split.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
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
    private User payer;

    /**
     * Purchase consumers
     */
    private List<User> consumers;
}
