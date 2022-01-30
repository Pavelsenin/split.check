package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.controllers.responses.UserResponse;

import java.util.Objects;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@CrossOrigin(origins="*")
public class UsersController {

    private final UserRepository userRepository;

    private final ConversionService conversionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    public UsersController(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    /**
     * Returns current user info
     *
     * @return
     */
    @GetMapping(path = "/current")
    @ResponseBody
    public ResponseEntity getCurrentUser() {
        User currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        UserResponse view = conversionService.convert(currentUser, UserResponse.class);
        return ResponseEntity.ok(view);
    }
}
