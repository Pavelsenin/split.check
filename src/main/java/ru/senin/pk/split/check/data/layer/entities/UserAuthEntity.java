package ru.senin.pk.split.check.data.layer.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthEntity {

    private String username;

    private String password;

    private Long userId;
}
