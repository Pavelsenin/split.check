package ru.senin.pk.split.check.data.layer.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
     * User checks id
     */
    private List<Long> checksIds;
}
