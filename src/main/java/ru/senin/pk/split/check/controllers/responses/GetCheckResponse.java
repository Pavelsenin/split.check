package ru.senin.pk.split.check.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCheckResponse {
    /**
     * Check id
     */
    private Long id;

    /**
     * Check name
     */
    private String name;

    /**
     * Check date
     */
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;

    /**
     * Check purchases
     */
    private List<Long> purchases;

    /**
     * Check users
     */
    private List<Long> users;

    /**
     * Check transfers
     */
    private List<TransferResponse> transfers;
}
