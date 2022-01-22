package ru.senin.pk.split.check.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.data.layer.dao.CheckDao;
import ru.senin.pk.split.check.data.layer.dao.PurchaseDao;
import ru.senin.pk.split.check.data.layer.dao.UserDao;
import ru.senin.pk.split.check.data.layer.dto.UserDto;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/checks", produces = "application/json")
@CrossOrigin(origins="*")
public class ChecksController {

    private final UserDao userDao;

    private final CheckDao checkDao;

    private final PurchaseDao purchaseDao;

    private final UserRepository userRepository;

    @Autowired
    public ChecksController(UserDao userDao, CheckDao checkDao, PurchaseDao purchaseDao, UserRepository userRepository) {
        this.userDao = userDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
        this.userRepository = userRepository;
    }

    /**
     * Returns list of check specified by user id
     *
     * @param userId
     * @return
     */
//    @GetMapping(path = "/get")
//    public String getUserChecks(@RequestParam("user_id") Long userId) {
//        Optional<UserEntity> userEntity = userDao.getUser(userId);
//        if (!userEntity.isPresent()) {
//            return "User not found";
//        }
//        List<CheckEntity> checkEntities = checkDao.getChecks(userEntity.get().getChecksIds());
//        return SerializationUtils.toString(userEntity.get()) + " " + SerializationUtils.toString(checkEntities);
//    }

    /**
     * Returns list of check specified by user id
     *
     * @param userId
     * @return
     */
    @GetMapping(path = "/get")
    @ResponseBody
    public ResponseEntity getUserChecks(@RequestParam("user_id") Long userId) {
//        Optional<UserEntity> userEntity = userDao.getUserById(userId);
//        if (!userEntity.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        List<CheckEntity> checkEntities = checkDao.getChecksByIds(userEntity.get().getChecksIds());
//        return ResponseEntity.ok(checkEntities);
        UserDto currentUser = userRepository.getUser(userId);
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currentUser.getChecks());
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
    public ResponseEntity addNewCheck(
            @RequestParam("user_id") Long userId,
            @RequestParam("check_name") String checkName,
            @RequestParam("check_date") @DateTimeFormat(pattern = "dd.MM.yyyy") Date checkDate
    ) {
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setName(checkName);
        checkEntity.setDate(checkDate);
        checkEntity.setUserIds(Stream.of(userId).collect(Collectors.toList()));
        checkDao.saveCheck(checkEntity);
        return ResponseEntity.ok(checkEntity);
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
    public ResponseEntity addCheckUser(
            @RequestParam("check_id") Long checkId,
            @RequestParam("new_user_id") Long newUserId
    ) {
        Optional<CheckEntity> checkEntityOpt = checkDao.getCheckById(checkId);
        if (!checkEntityOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Optional<UserEntity> userEntityOpt = userDao.getUserById(newUserId);
        if (!userEntityOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CheckEntity checkEntity = checkEntityOpt.get();
        return ResponseEntity.ok("");

    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception ex) {
        return ResponseEntity.internalServerError().build();
    }
}
