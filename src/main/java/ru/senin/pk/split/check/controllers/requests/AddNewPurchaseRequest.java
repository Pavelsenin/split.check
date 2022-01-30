package ru.senin.pk.split.check.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNewPurchaseRequest {
    /**
     * Purchase name
     */
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    /**
     * Purchase name
     */
    @NotNull
    @Positive
    private BigDecimal cost;

    /**
     * Purchase payer id
     */
    @NotNull
    private Long payer;

    /**
     * Purchase consumers ids
     */
    @NotNull
    @Size(min = 1, max = 100)
    private List<Long> consumers;
}
