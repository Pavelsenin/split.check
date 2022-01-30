package ru.senin.pk.split.check.data.layer.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
