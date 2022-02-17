package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.controllers.requests.SignInRequest;
import ru.senin.pk.split.check.controllers.requests.SignOutRequest;
import ru.senin.pk.split.check.controllers.requests.SignUpRequest;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;
import ru.senin.pk.split.check.controllers.responses.SignInResponse;
import ru.senin.pk.split.check.controllers.responses.SignUpResponse;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.errors.UserAlreadyExistsException;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.UserAuth;
import ru.senin.pk.split.check.services.UserAuthService;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(path = "/auth", produces = "application/json")
@CrossOrigin(origins = "*")
@Validated
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserAuthService userAuthService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserAuthService userAuthService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAuthService = userAuthService;
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
        userAuthService.userSignUp(user);
        response = new SignUpResponse(true);
        LOGGER.info("User sign up success. response: {}", response);
        return response;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserAlreadyExist(UserAlreadyExistsException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        LOGGER.info("User sign up failed, user already exists. response: {}", response);
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @PostMapping(path = "/signin")
    public SignInResponse userSignIn(@RequestBody @Valid SignInRequest request) {
        LOGGER.info("User sign in request. request: {}", request);
        CurrentUser currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return new SignInResponse(false);
        } else {
            return new SignInResponse(true);
        }
    }

    @PostMapping(path = "/signout")
    public void userSignOut(@RequestBody @Valid SignOutRequest request) {
        LOGGER.info("User sign out request. request: {}", request);
    }
}
