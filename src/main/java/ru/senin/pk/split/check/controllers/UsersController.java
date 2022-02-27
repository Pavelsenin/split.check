package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.controllers.responses.UserResponse;
import ru.senin.pk.split.check.services.UserAuthService;
import ru.senin.pk.split.check.validation.ValidatedAccess;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@CrossOrigin(origins = "*")
public class UsersController {

    private final UserAuthService userAuthService;

    private final ConversionService conversionService;

    private final ValidatedAccess validatedAccess;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    public UsersController(UserAuthService userAuthService, ConversionService conversionService, ValidatedAccess validatedAccess) {
        this.userAuthService = userAuthService;
        this.conversionService = conversionService;
        this.validatedAccess = validatedAccess;
    }

    /**
     * Returns current user info
     *
     * @return
     */
    @GetMapping(path = "/current")
    @ResponseBody
    public UserResponse getCurrentUser() {
        LOGGER.info("Get current user");
        CurrentUser currentUser = userAuthService.getCurrentUser();
        validatedAccess.validateCurrentUserFound(currentUser);
        UserResponse response = conversionService.convert(currentUser, UserResponse.class);
        LOGGER.info("Current user found. response: {}", response);
        return response;
    }
}
