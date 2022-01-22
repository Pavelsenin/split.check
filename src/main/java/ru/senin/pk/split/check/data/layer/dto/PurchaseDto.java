package ru.senin.pk.split.check.data.layer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
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
    private UserDto payer;

    /**
     * Purchase consumers
     */
    private List<UserDto> consumers;
}
