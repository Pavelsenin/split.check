package ru.senin.pk.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserEntity {
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
    private List<CheckEntity> checks;
}
