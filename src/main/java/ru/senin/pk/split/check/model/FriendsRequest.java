package ru.senin.pk.split.check.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendsRequest {

    private RegisteredUser sourceUser;

    private RegisteredUser targetUser;
}
