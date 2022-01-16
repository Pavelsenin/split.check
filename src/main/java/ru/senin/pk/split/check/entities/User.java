package ru.senin.pk.split.check.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * User id
     */
    private Long id;

    /**
     * User name
     */
    private String name;

    /**
     * User checks
     */
    private List<Check> checksIds;
}
