package ru.senin.pk.split.check.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    /**
     * User id
     */
    private Long id;

    /**
     * User name
     */
    private String name;
}
