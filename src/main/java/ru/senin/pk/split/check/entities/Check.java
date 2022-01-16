package ru.senin.pk.split.check.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private Date date;

    /**
     * Check purchases
     */
    private List<Purchase> purchases;

    /**
     * Check users
     */
    private List<User> users;
}
