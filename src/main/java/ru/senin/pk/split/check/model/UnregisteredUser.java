package ru.senin.pk.split.check.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnregisteredUser extends User {

    public UnregisteredUser(Long id, String name) {
        super(id, name);
    }
}
