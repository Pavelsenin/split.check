package ru.senin.pk.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class CheckEntity {
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
    private List<PurchaseEntity> purchases;

    /**
     * Check users
     */
    private List<UserEntity> users;
}
