package ru.senin.pk.split.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Check {
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
    private LocalDate date;

    /**
     * Check purchases ids
     */
    private List<Purchase> purchases;

    /**
     * Check users ids
     */
    private List<User> users;

    /**
     * Check transfers
     */
    private List<Transfer> transfers;
}
