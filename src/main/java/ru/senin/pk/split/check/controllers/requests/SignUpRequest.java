package ru.senin.pk.split.check.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotNull
    @Size(min = 1, max = 25)
    private String username;

    @NotNull
    @Size(min = 1, max = 120)
    private String password;
}
