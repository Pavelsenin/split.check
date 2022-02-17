package ru.senin.pk.split.check.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.errors.UserAlreadyExistsException;
import ru.senin.pk.split.check.model.CurrentUser;

import java.util.Objects;

@Service
public class UserAuthService {

    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthService.class);

    @Autowired
    public UserAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Signs up new user
     *
     * @param user
     * @return new user
     * @throws UserAlreadyExistsException if user with username already exists
     */
    public CurrentUser userSignUp(CurrentUser user) {
        LOGGER.info("User sign up. user: {}", user);
        if (Objects.nonNull(userRepository.getCurrentUserByUsername(user.getAuth().getUsername()))) {
            throw new UserAlreadyExistsException("Account with username already registered. username: " + user.getAuth().getUsername());
        }
        userRepository.saveCurrentUser(user);
        LOGGER.info("User signed up. user: {}", user);
        return user;
    }
}
