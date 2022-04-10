package ru.senin.pk.split.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    /**
     * Returns list of friends
     *
     * @return List
     */
    public List<RegisteredUser> getFriends() {
        List<FriendsRequest> acceptedFriendRequests = getUserFriendRequests().getAcceptedFriendsRequests();
        List<RegisteredUser> friends = new ArrayList<>(acceptedFriendRequests.size());
        for (FriendsRequest friendsRequest : acceptedFriendRequests) {
            if (Objects.equals(getId(), friendsRequest.getSourceUser().getId())) {
                friends.add(friendsRequest.getTargetUser());
            } else {
                friends.add(friendsRequest.getSourceUser());
            }
        }
        return friends;
    }
}
