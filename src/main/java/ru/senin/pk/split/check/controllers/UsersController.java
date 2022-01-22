package ru.senin.pk.split.check.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.data.layer.dao.CheckDao;
import ru.senin.pk.split.check.data.layer.dao.PurchaseDao;
import ru.senin.pk.split.check.data.layer.dao.UserDao;
import ru.senin.pk.split.check.data.layer.dto.UserDto;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@CrossOrigin(origins="*")
public class UsersController {

    private final UserDao userDao;

    private final CheckDao checkDao;

    private final PurchaseDao purchaseDao;

    private final UserRepository userRepository;

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
     * @param userId
     * @return
     */
    @GetMapping(path = "/current")
    @ResponseBody
    public ResponseEntity getCurrentUser(@RequestParam("user_id") Long userId) { // TODO remove stub param
//        Optional<UserEntity> userEntity = userDao.getUserById(userId);
//        if (!userEntity.isPresent()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(userEntity.get());
//        }
        UserDto currentUser = userRepository.getUser(userId);
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(currentUser);
        }
    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception ex) {
        return ResponseEntity.internalServerError().build();
    }
}
