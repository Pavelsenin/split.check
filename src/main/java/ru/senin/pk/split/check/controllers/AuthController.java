package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.controllers.requests.SignOutRequest;
import ru.senin.pk.split.check.controllers.requests.SignUpRequest;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;
import ru.senin.pk.split.check.controllers.responses.SignUpResponse;
import ru.senin.pk.split.check.errors.UserAlreadyExistsException;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.UserAuth;
import ru.senin.pk.split.check.services.UserAuthService;
import ru.senin.pk.split.check.validation.FieldValidationError;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/auth", produces = "application/json")
@CrossOrigin(origins = "*")
@Validated
public class AuthController {

    private final UserAuthService userAuthService;

    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserAuthService userAuthService, PasswordEncoder passwordEncoder) {
        this.userAuthService = userAuthService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/signup")
    @ResponseBody
    public SignUpResponse userSignUp(@RequestBody @Valid SignUpRequest request) {
        LOGGER.info("User sign up request. request: {}", request);
        CurrentUser user = new CurrentUser();
        user.setName(request.getUsername());
        UserAuth auth = new UserAuth();
        user.setAuth(auth);
        auth.setUsername(request.getUsername());
        auth.setPassword(passwordEncoder.encode(request.getPassword()));
        SignUpResponse response;
        userAuthService.signUpUser(user);
        response = new SignUpResponse(true);
        LOGGER.info("User sign up success. response: {}", response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleUserAlreadyExist(UserAlreadyExistsException e) {
        FieldValidationError fieldValidationError = new FieldValidationError("username", e.getMessage() + " " +e.getUsername());
        ErrorResponse response = new ErrorResponse(e.getMessage(), Arrays.asList(fieldValidationError));
        LOGGER.info("User sign up failed, user already exists. response: {}", response);
        return response;
    }

    @PostMapping(path = "/signout")
    public void userSignOut(@RequestBody @Valid SignOutRequest request) {
        LOGGER.info("User sign out request. request: {}", request);
    }
}
