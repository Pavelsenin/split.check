package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.data.layer.dao.CheckDao;
import ru.senin.pk.split.check.data.layer.dao.PurchaseDao;
import ru.senin.pk.split.check.data.layer.dao.UserDao;
import ru.senin.pk.split.check.data.layer.dto.UserDto;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;

import java.util.Objects;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@CrossOrigin(origins="*")
public class UsersController {

    private final UserDao userDao;

    private final CheckDao checkDao;

    private final PurchaseDao purchaseDao;

    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    public UsersController(UserDao userDao, CheckDao checkDao, PurchaseDao purchaseDao, UserRepository userRepository) {
        this.userDao = userDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
        this.userRepository = userRepository;
    }

    /**
     * Returns current user info
     *
     * @return
     */
    @GetMapping(path = "/current")
    @ResponseBody
    public ResponseEntity getCurrentUser() {
//        Optional<UserEntity> userEntity = userDao.getUserById(userId);
//        if (!userEntity.isPresent()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(userEntity.get());
//        }
        UserDto currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(currentUser);
        }
    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception ex) {
        LOGGER.error("Error: ", ex);
        return ResponseEntity.internalServerError().build();
    }
}
