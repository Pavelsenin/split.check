package ru.senin.pk.split.check.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisteredUser extends User {

    public RegisteredUser(Long id, String name) {
        super(id, name);
    }
}
