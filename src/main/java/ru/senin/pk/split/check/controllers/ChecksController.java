package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.controllers.requests.AddNewCheckRequest;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.RegisteredUser;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.controllers.responses.GetCheckResponse;
import ru.senin.pk.split.check.services.CheckTransfersService;
import ru.senin.pk.split.check.services.UserAuthService;
import ru.senin.pk.split.check.validation.ValidatedAccess;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/checks", produces = "application/json")
@CrossOrigin(origins = "*")
@Validated
public class ChecksController {

    private final UserAuthService userAuthService;

    private final UserRepository userRepository;

    private final ConversionService conversionService;

    private final ValidatedAccess validatedAccess;

    private final CheckTransfersService checkTransfersService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksController.class);

    @Autowired
    public ChecksController(UserAuthService userAuthService, UserRepository userRepository, ConversionService conversionService, ValidatedAccess validatedAccess, CheckTransfersService checkTransfersService) {
        this.userAuthService = userAuthService;
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        this.validatedAccess = validatedAccess;
        this.checkTransfersService = checkTransfersService;
    }

    /**
     * Returns list of checks of current user
     *
     * @param userId
     * @return
     */
    @GetMapping(path = "/get")
    @ResponseBody
    public List<GetCheckResponse> getCurrentUserChecks() {
        LOGGER.info("Get current user checks");
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        List<Check> checks = currentUser.getChecks();
        checks.stream().forEach(checkTransfersService::calculateTransfers);
        List<GetCheckResponse> response = currentUser.getChecks().stream()
                .map(check -> conversionService.convert(check, GetCheckResponse.class))
                .collect(Collectors.toList());
        LOGGER.info("Checks found. checks: {}, response: {}", checks, response);
        return response;
    }

    /**
     * Adds new check specified by user id
     *
     * @param userId
     * @param checkName
     * @param checkDate
     * @return
     */
    @PostMapping(path = "/new")
    @ResponseBody
    public GetCheckResponse addNewCheck(
            @RequestBody @Valid AddNewCheckRequest request
    ) {
        LOGGER.info("Add new check. request: {}", request);
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);

        Check newCheck = new Check();
        newCheck.setName(request.getName());
        newCheck.setDate(request.getDate());
        newCheck.setUsers(Stream.of(new RegisteredUser(currentUser.getId(), currentUser.getName())).collect(Collectors.toList()));
        newCheck.setPurchases(Collections.emptyList());
        newCheck.setTransfers(Collections.emptyList());
        currentUser.getChecks().add(newCheck);
        userRepository.saveCurrentUser(currentUser);
        checkTransfersService.calculateTransfers(newCheck);

        GetCheckResponse response = conversionService.convert(newCheck, GetCheckResponse.class);
        LOGGER.info("New check added. newCheck: {}, response: {}", newCheck, response);
        return response;
    }

    /**
     * Adds new check user
     *
     * @param userId
     * @param checkName
     * @param checkDate
     * @return
     */
    @PostMapping(path = "/add_user")
    @ResponseBody
    public GetCheckResponse addCheckUser(
            @RequestParam("check_id") Long checkId,
            @RequestParam("new_user_id") Long newUserId
    ) {
        LOGGER.info("Add check user. checkId: {}, newUserId", checkId, newUserId);
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        Check check = validatedAccess.getCurrentUserCheck(currentUser, checkId);
        boolean userAlreadyAdded = check.getUsers().stream()
                .filter(x -> Objects.equals(newUserId, x.getId()))
                .findAny()
                .isPresent();
        if (userAlreadyAdded) {
            GetCheckResponse response = conversionService.convert(check, GetCheckResponse.class);
            LOGGER.info("Check user already added. response: {}", response);
            return response;
        }
        User newUser = userRepository.getRegisteredUserById(newUserId);
        validatedAccess.validateUserFound(newUser);
        check.getUsers().add(newUser);
        check.setPurchases(Collections.emptyList());
        userRepository.saveCurrentUser(currentUser);
        GetCheckResponse response = conversionService.convert(check, GetCheckResponse.class);
        LOGGER.info("Check user added. response: {} ", response);
        return response;
    }

    /**
     * Adds new check user
     *
     * @param userId
     * @param checkName
     * @param checkDate
     * @return
     */
    @PostMapping(path = "/remove_user")
    @ResponseBody
    public GetCheckResponse removeCheckUser(
            @RequestParam("check_id") Long checkId,
            @RequestParam("remove_user_id") Long removeUserId
    ) {
        LOGGER.info("Remove check user. checkId: {}, removeUserId", checkId, removeUserId);
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        Check check = validatedAccess.getCurrentUserCheck(currentUser, checkId);
        Optional<User> userToRemoveOpt = check.getUsers().stream()
                .filter(x -> Objects.equals(removeUserId, x.getId()))
                .findAny();
        if (!userToRemoveOpt.isPresent()) {
            GetCheckResponse response = conversionService.convert(check, GetCheckResponse.class);
            LOGGER.info("Check user already removed. response: {}", response);
            return response;
        }
        check.getUsers().remove(userToRemoveOpt.get());
        userRepository.saveCurrentUser(currentUser);
        GetCheckResponse response = conversionService.convert(check, GetCheckResponse.class);
        LOGGER.info("Check user removed. response: {}", response);
        return response;
    }
}
