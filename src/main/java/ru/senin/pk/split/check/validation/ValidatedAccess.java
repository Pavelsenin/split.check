package ru.senin.pk.split.check.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.errors.ServiceValidationException;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.User;

import java.util.Objects;

/**
 * Bean provides validated access to {@link CurrentUser} object fields.
 * If validation errors occurred, throws {@link ServiceValidationException}.
 */
@Component
public class ValidatedAccess {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatedAccess.class);

    /**
     * Validate current user found
     *
     * @param currentUser
     * @throws ServiceValidationException if current user not found
     */
    public void validateCurrentUserFound(CurrentUser currentUser) {
        if (Objects.isNull(currentUser)) {
            LOGGER.info("Current user not found");
            throw new ServiceValidationException("Current user not found");
        }
    }

    /**
     * Validate not current user by id
     *
     * @param currentUser
     * @throws ServiceValidationException if current user not found
     */
    public void validateNotCurrentUser(CurrentUser currentUser, Long userId) {
        if (Objects.equals(currentUser.getId(), userId)) {
            LOGGER.info("Current user id used");
            throw new ServiceValidationException("Current user id used");
        }
    }


    /**
     * Validate current user has check with specified id
     *
     * @param currentUser
     * @param checkId
     * @throws ServiceValidationException if current user has no check with specified id
     */
    public void validateCurrentUserHasCheck(CurrentUser currentUser, Long checkId) {
        Check check = currentUser.getChecks().stream()
                .filter(x -> Objects.equals(x.getId(), checkId))
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            LOGGER.info("Check not found for current user. check id: {}, user: {}", checkId, currentUser);
            throw new ServiceValidationException("Check not found for current user");
        }
    }

    /**
     * Returns current user check with specified id
     *
     * @param currentUser
     * @param checkId
     * @throws ServiceValidationException if current user has no check with specified id
     */
    public Check getCurrentUserCheck(CurrentUser currentUser, Long checkId) {
        Check check = currentUser.getChecks().stream()
                .filter(x -> Objects.equals(x.getId(), checkId))
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            LOGGER.info("Check not found for current user. check id: {}, user: {}", checkId, currentUser);
            throw new ServiceValidationException("Check not found for current user");
        }
        return check;
    }

    /**
     * Returns check user with specified id
     *
     * @param check
     * @param userId
     * @throws ServiceValidationException if check has no user with specified id
     */
    public User getCheckUser(Check check, Long userId) {
        User user = check.getUsers().stream()
                .filter(x -> Objects.equals(x.getId(), userId))
                .findAny()
                .orElse(null);
        if (Objects.isNull(user)) {
            LOGGER.info("User not found in check. user id: {}, check: {}", userId, check);
            throw new ServiceValidationException("User not found in check");
        }
        return user;
    }

    /**
     * Validate user found
     *
     * @param currentUser
     * @throws ServiceValidationException if user not found
     */
    public void validateUserFound(User currentUser) {
        if (Objects.isNull(currentUser)) {
            LOGGER.info("User not found");
            throw new ServiceValidationException("User not found");
        }
    }
}
