package ru.senin.pk.split.check.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CurrentUser extends User {

    public CurrentUser(Long id, String name, List<Check> checks) {
        super(id, name);
        this.checks = checks;
    }

    /**
     * User checks id
     */
    private List<Check> checks;
}
