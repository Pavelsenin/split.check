package ru.senin.pk.split.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    /**
     * User id
     */
    @Nullable
    private Long id;

    /**
     * User name
     */
    @NotNull
    private String name;
}
