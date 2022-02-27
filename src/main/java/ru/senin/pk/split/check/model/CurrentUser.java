package ru.senin.pk.split.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser extends User {

    private UserAuth auth;

    /**
     * User checks id
     */
    private List<Check> checks = Collections.emptyList();

    /**
     * User friend requests
     */
    private UserFriendRequests userFriendRequests = new UserFriendRequests();

    public CurrentUser(Long id, String name, List<Check> checks, UserFriendRequests userFriendRequests) {
        super(id, name);
        this.checks = checks;
        this.userFriendRequests = userFriendRequests;
    }
}
