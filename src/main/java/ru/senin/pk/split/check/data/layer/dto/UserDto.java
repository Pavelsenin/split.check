package ru.senin.pk.split.check.data.layer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
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
    private List<CheckDto> checks;
}
