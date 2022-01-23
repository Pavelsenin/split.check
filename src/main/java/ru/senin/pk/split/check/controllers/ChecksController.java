package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.auth.CurrentUserService;
import ru.senin.pk.split.check.data.layer.dao.CheckDao;
import ru.senin.pk.split.check.data.layer.dao.PurchaseDao;
import ru.senin.pk.split.check.data.layer.dao.UserDao;
import ru.senin.pk.split.check.data.layer.dto.CheckDto;
import ru.senin.pk.split.check.data.layer.dto.UserDto;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/checks", produces = "application/json")
@CrossOrigin(origins = "*")
public class ChecksController {

    private final UserDao userDao;

    private final CheckDao checkDao;

    private final PurchaseDao purchaseDao;

    private final CurrentUserService currentUserService;

    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksController.class);

    @Autowired
    public ChecksController(UserDao userDao, CheckDao checkDao, PurchaseDao purchaseDao, CurrentUserService currentUserService, UserRepository userRepository) {
        this.userDao = userDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    /**
     * Returns list of checks of current user
     *
     * @param userId
     * @return
     */
    @GetMapping(path = "/get")
    @ResponseBody
    public ResponseEntity getCurrentUserChecks() {
        UserDto currentUser = userRepository.getCurrentUser();
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
            @RequestParam("check_name") String checkName,
            @RequestParam("check_date") @DateTimeFormat(pattern = "dd.MM.yyyy") Date checkDate
    ) {
        UserDto currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        CheckDto newCheck = new CheckDto();
        newCheck.setName(checkName);
        newCheck.setDate(checkDate);
        newCheck.setUsers(Stream.of(currentUser).collect(Collectors.toList()));
        newCheck.setPurchases(new ArrayList<>());
        currentUser.getChecks().add(newCheck);
        userRepository.saveUser(currentUser);

        return ResponseEntity.ok(newCheck);
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
        UserDto currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        CheckDto check = currentUser.getChecks().stream()
                .filter(checkId::equals)
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            return ResponseEntity.notFound().build();
        }
        boolean userAlreadyAdded = check.getUsers().stream()
                .filter(newUserId::equals)
                .findAny()
                .isPresent();
        if (userAlreadyAdded) {
            return ResponseEntity.ok(check);
        }
        UserDto newUser = userRepository.getUser(newUserId);
        if (Objects.isNull(newUser)) {
            return ResponseEntity.notFound().build();
        }
        check.getUsers().add(newUser);
        check.setPurchases(Collections.emptyList());
        userRepository.saveUser(currentUser);
        return ResponseEntity.ok(check);
    }

    /**
     * Adds new check user
     *
     * @param userId
     * @param checkName
     * @param checkDate
     * @return
     */
    @PostMapping(path = "/remove_user")
    @ResponseBody
    public ResponseEntity removeCheckUser(
            @RequestParam("check_id") Long checkId,
            @RequestParam("remove_user_id") Long removeUserId
    ) {
        UserDto currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        CheckDto check = currentUser.getChecks().stream()
                .filter(checkId::equals)
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            return ResponseEntity.notFound().build();
        }
        Optional<UserDto> userToRemoveOpt = check.getUsers().stream()
                .filter(removeUserId::equals)
                .findAny();
        if (!userToRemoveOpt.isPresent()) {
            return ResponseEntity.ok(check);
        }
        check.getUsers().remove(userToRemoveOpt.get());
        userRepository.saveUser(currentUser);
        return ResponseEntity.ok(check);
    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception ex) {
        LOGGER.error("Error: ", ex);
        return ResponseEntity.internalServerError().build();
    }
}
