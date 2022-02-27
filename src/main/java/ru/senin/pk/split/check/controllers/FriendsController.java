package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.controllers.requests.AcceptFriendRequest;
import ru.senin.pk.split.check.controllers.requests.AddFriendRequest;
import ru.senin.pk.split.check.controllers.requests.DeclineFriendRequest;
import ru.senin.pk.split.check.controllers.requests.RemoveFriendRequest;
import ru.senin.pk.split.check.controllers.responses.*;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.model.*;
import ru.senin.pk.split.check.services.UserAuthService;
import ru.senin.pk.split.check.validation.ValidatedAccess;

import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/friends", produces = "application/json")
@CrossOrigin(origins = "*")
@Validated
public class FriendsController {

    private final UserAuthService userAuthService;

    private final UserRepository userRepository;

    private final ValidatedAccess validatedAccess;

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendsController.class);

    @Autowired
    public FriendsController(UserAuthService userAuthService, UserRepository userRepository, ValidatedAccess validatedAccess) {
        this.userAuthService = userAuthService;
        this.userRepository = userRepository;
        this.validatedAccess = validatedAccess;
    }

    /**
     * Returns current user friends info
     *
     * @return
     */
    @GetMapping(path = "/get")
    @ResponseBody
    public GetFriendsResponse getCurrentUserFriends() {
        LOGGER.info("Get current user friends");
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        GetFriendsResponse response = new GetFriendsResponse();
        UserFriendRequests userFriendRequests = currentUser.getUserFriendRequests();
        response.setFriends(userFriendRequests.getAcceptedFriendsRequests().stream()
                .map(friendRequest -> getAcceptedFriendId(currentUser.getId(), friendRequest))
                .collect(Collectors.toList())
        );
        response.setIncomingFriendRequests(userFriendRequests.getIncomingFriendsRequests().stream()
                .map(friendRequest -> friendRequest.getSourceUser().getId())
                .collect(Collectors.toList())
        );
        response.setOutgoingFriendRequests(userFriendRequests.getOutgoingFriendsRequests().stream()
                .map(friendRequest -> friendRequest.getTargetUser().getId())
                .collect(Collectors.toList())
        );
        LOGGER.info("Get current user friends. response: {}", response);
        return response;
    }

    private Long getAcceptedFriendId(Long currentUserId, FriendsRequest friendsRequest) {
        if (Objects.equals(currentUserId, friendsRequest.getSourceUser().getId())) {
            return friendsRequest.getTargetUser().getId();
        } else {
            return friendsRequest.getSourceUser().getId();
        }
    }

    @PostMapping(path = "/add")
    @ResponseBody
    public AddFriendResponse addFriend(
            @RequestBody @Valid AddFriendRequest request
    ) {
        LOGGER.info("Add friend. request: {}", request);
        AddFriendResponse response;
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        validatedAccess.validateNotCurrentUser(currentUser, request.getId());
        UserFriendRequests userFriendRequests = currentUser.getUserFriendRequests();
        for (FriendsRequest acceptedFriendsRequest : userFriendRequests.getAcceptedFriendsRequests()) {
            if (Objects.equals(request.getId(), getAcceptedFriendId(currentUser.getId(), acceptedFriendsRequest))) {
                LOGGER.debug("Friend request already added on add. friendRequest: {}", acceptedFriendsRequest);
                response = new AddFriendResponse(true);
                LOGGER.info("Add friend. response: {}", response);
                return response;
            }
        }
        for (FriendsRequest outgoingFriendsRequest : userFriendRequests.getOutgoingFriendsRequests()) {
            if (Objects.equals(request.getId(), getAcceptedFriendId(currentUser.getId(), outgoingFriendsRequest))) {
                LOGGER.debug("Friend request already sent. friendRequest: {}", outgoingFriendsRequest);
                response = new AddFriendResponse(true);
                LOGGER.info("Add friend. response: {}", response);
                return response;
            }
        }
        for (FriendsRequest incomingFriendsRequest : userFriendRequests.getIncomingFriendsRequests()) {
            if (Objects.equals(request.getId(), getAcceptedFriendId(currentUser.getId(), incomingFriendsRequest))) {
                LOGGER.debug("Found incoming pending friend request on add. friendRequest: {}", incomingFriendsRequest);
                userFriendRequests.getIncomingFriendsRequests().remove(incomingFriendsRequest);
                userFriendRequests.getAcceptedFriendsRequests().add(incomingFriendsRequest);
                userRepository.saveCurrentUser(currentUser);
                // TODO notification
                LOGGER.debug("Friend request changed to accepted on add. friendRequest: {}", incomingFriendsRequest);
                response = new AddFriendResponse(true);
                LOGGER.info("Add friend. response: {}", response);
                return response;
            }
        }
        RegisteredUser targetUser = userRepository.getRegisteredUserById(request.getId());
        validatedAccess.validateUserFound(targetUser);
        FriendsRequest newOutgoingFriendsRequest = new FriendsRequest(new RegisteredUser(currentUser.getId(), currentUser.getName()), targetUser);
        userFriendRequests.getOutgoingFriendsRequests().add(newOutgoingFriendsRequest);
        userRepository.saveCurrentUser(currentUser);
        // TODO notification
        response = new AddFriendResponse(true);
        LOGGER.info("Add friend. response: {}", response);
        return response;
    }

    @PostMapping(path = "/accept")
    @ResponseBody
    public AcceptFriendResponse acceptFriend(
            @RequestBody @Valid AcceptFriendRequest request
    ) {
        LOGGER.info("Accept friend. request: {}", request);
        AcceptFriendResponse response;
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        UserFriendRequests userFriendRequests = currentUser.getUserFriendRequests();
        for (FriendsRequest acceptedFriendsRequest : userFriendRequests.getAcceptedFriendsRequests()) {
            if (Objects.equals(request.getId(), getAcceptedFriendId(currentUser.getId(), acceptedFriendsRequest))) {
                LOGGER.debug("Friend request already accepted on accept. friendRequest: {}", acceptedFriendsRequest);
                response = new AcceptFriendResponse(true);
                LOGGER.info("Accept friend. response: {}", response);
                return response;
            }
        }
        for (FriendsRequest incomingFriendsRequest : userFriendRequests.getIncomingFriendsRequests()) {
            if (Objects.equals(request.getId(), getAcceptedFriendId(currentUser.getId(), incomingFriendsRequest))) {
                LOGGER.debug("Found incoming pending friend request on accept. friendRequest: {}", incomingFriendsRequest);
                userFriendRequests.getIncomingFriendsRequests().remove(incomingFriendsRequest);
                userFriendRequests.getAcceptedFriendsRequests().add(incomingFriendsRequest);
                userRepository.saveCurrentUser(currentUser);
                // TODO notification
                LOGGER.debug("Friend request changed to accepted on accept. friendRequest: {}", incomingFriendsRequest);
                response = new AcceptFriendResponse(true);
                LOGGER.info("Accept friend. response: {}", response);
                return response;
            }
        }
        LOGGER.debug("Incoming pending friend request not found on accept");
        response = new AcceptFriendResponse(false);
        LOGGER.info("Accept friend. response: {}", response);
        return response;
    }

    @PostMapping(path = "/decline")
    @ResponseBody
    public DeclineFriendResponse declineFriend(
            @RequestBody @Valid DeclineFriendRequest request
    ) {
        LOGGER.info("Decline friend. request: {}", request);
        DeclineFriendResponse response;
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        UserFriendRequests userFriendRequests = currentUser.getUserFriendRequests();
        for (FriendsRequest incomingFriendsRequest : userFriendRequests.getIncomingFriendsRequests()) {
            if (Objects.equals(request.getId(), getAcceptedFriendId(currentUser.getId(), incomingFriendsRequest))) {
                LOGGER.debug("Found incoming pending friend request on decline. friendRequest: {}", incomingFriendsRequest);
                userFriendRequests.getIncomingFriendsRequests().remove(incomingFriendsRequest);
                userRepository.saveCurrentUser(currentUser);
                // TODO notification
                LOGGER.debug("Friend request declined. friendRequest: {}", incomingFriendsRequest);
                response = new DeclineFriendResponse(true);
                LOGGER.info("Decline friend. response: {}", response);
                return response;
            }
        }
        LOGGER.debug("Incoming pending friend request not found on decline");
        response = new DeclineFriendResponse(false);
        LOGGER.info("Decline friend. response: {}", response);
        return response;
    }

    @PostMapping(path = "/remove")
    @ResponseBody
    public RemoveFriendResponse removeFriend(
            @RequestBody @Valid RemoveFriendRequest request
    ) {
        LOGGER.info("Remove friend. request: {}", request);
        RemoveFriendResponse response;
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        UserFriendRequests userFriendRequests = currentUser.getUserFriendRequests();
        for (FriendsRequest acceptedFriendsRequest : userFriendRequests.getAcceptedFriendsRequests()) {
            if (Objects.equals(request.getId(), getAcceptedFriendId(currentUser.getId(), acceptedFriendsRequest))) {
                LOGGER.debug("Found accepted friend request on remove. friendRequest: {}", acceptedFriendsRequest);
                userFriendRequests.getAcceptedFriendsRequests().remove(acceptedFriendsRequest);
                userRepository.saveCurrentUser(currentUser);
                // TODO notification
                LOGGER.debug("Friend request removed. friendRequest: {}", acceptedFriendsRequest);
                response = new RemoveFriendResponse(true);
                LOGGER.info("Remove friend. response: {}", response);
                return response;
            }
        }
        LOGGER.debug("Accepted friend request not found on remove");
        response = new RemoveFriendResponse(false);
        LOGGER.info("Remove friend. response: {}", response);
        return response;
    }
}
