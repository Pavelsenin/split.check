package ru.senin.pk.split.check.services;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.errors.UserAlreadyExistsException;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.UserAuth;

import java.util.Objects;

@Service
public class UserAuthServiceImpl implements UserAuthService, UserDetailsService {

    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    @Autowired
    public UserAuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CurrentUser signUpUser(CurrentUser user) {
        LOGGER.info("User sign up. user: {}", user);
        if (Objects.nonNull(userRepository.getCurrentUserByUsername(user.getAuth().getUsername()))) {
            throw new UserAlreadyExistsException("Account with username already registered. username: " + user.getAuth().getUsername());
        }
        userRepository.saveCurrentUser(user);
        LOGGER.info("User signed up. user: {}", user);
        return user;
    }

    @Override
    public CurrentUser getCurrentUser() {
        LOGGER.info("Get current user by session");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            LOGGER.info("Current user session not available");
            return null;
        }
        Validate.notNull(authentication.getPrincipal(), "Auth principal missed");
        Validate.isInstanceOf(UserAuth.class, authentication.getPrincipal(), "Auth principal is not instance of UserAuth");
        UserAuth userAuth = UserAuth.class.cast(authentication.getPrincipal());
        CurrentUser currentUser = userRepository.getCurrentUserByUsername(userAuth.getUsername());
        Validate.notNull(currentUser, "User not found for session. username: " + userAuth.getUsername());
        LOGGER.info("User found for session. username: {}", userAuth.getUsername());
        return currentUser;
    }

    @Override
    public UserAuth loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Load user by username. username: {}", username);
        CurrentUser user = userRepository.getCurrentUserByUsername(username);
        if (Objects.isNull(user)) {
            LOGGER.info("User not found by username. username: {}", username);
            throw new UsernameNotFoundException("User not found by username. username: " + username);
        }
        LOGGER.info("User found by username. username: {}", username);
        return user.getAuth();
    }
}
