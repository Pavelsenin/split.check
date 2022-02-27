package ru.senin.pk.split.check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendRequests {

    /**
     * Accepted friends requests
     */
    private List<FriendsRequest> acceptedFriendsRequests = new ArrayList<>();

    /**
     * Incoming friends requests
     */
    private List<FriendsRequest> incomingFriendsRequests = new ArrayList<>();

    /**
     * Outgoing friends requests
     */
    private List<FriendsRequest> outgoingFriendsRequests = new ArrayList<>();
}
