package ru.senin.pk.split.check.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignOutRequest {

    @NonNull
    @Size(min = 1, max = 25)
    private String username;

    @NonNull
    @Size(min = 1, max = 120)
    private String password;
}
