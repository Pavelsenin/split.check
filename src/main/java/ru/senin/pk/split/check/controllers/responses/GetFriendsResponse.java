package ru.senin.pk.split.check.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFriendsResponse {

    /**
     * Friend users ids
     */
    private List<Long> friends;

    /**
     * Outgoing friend requests users ids
     */
    private List<Long> outgoingFriendRequests;

    /**
     * Incoming friend requests users ids
     */
    private List<Long> incomingFriendRequests;
}
