package ru.senin.pk.split.check.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptFriendRequest {

    @NotNull
    private Long id;
}
