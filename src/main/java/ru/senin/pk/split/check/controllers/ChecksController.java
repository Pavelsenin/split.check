package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.controllers.requests.AddNewCheckRequest;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.controllers.responses.CheckResponse;
import ru.senin.pk.split.check.utils.SerializationUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/checks", produces = "application/json")
@CrossOrigin(origins = "*")
public class ChecksController {

    private final UserRepository userRepository;

    private final ConversionService conversionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksController.class);

    @Autowired
    public ChecksController(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
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
        CurrentUser currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        List<CheckResponse> view = currentUser.getChecks().stream()
                .map(check -> conversionService.convert(check, CheckResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(view);
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
        CurrentUser currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        Check newCheck = new Check();
        newCheck.setName(checkName);
        newCheck.setDate(checkDate);
        newCheck.setUsers(Stream.of(currentUser).collect(Collectors.toList()));
        newCheck.setPurchases(new ArrayList<>());
        currentUser.getChecks().add(newCheck);
        userRepository.saveCurrentUser(currentUser);

        CheckResponse view = conversionService.convert(newCheck, CheckResponse.class);
        return ResponseEntity.ok(view);
    }
//    /**
//     * Adds new check specified by user id
//     *
//     * @param userId
//     * @param checkName
//     * @param checkDate
//     * @return
//     */
//    @PostMapping(path = "/new")
//    @ResponseBody
//    public ResponseEntity addNewCheck(
//            @RequestBody AddNewCheckRequest request
//    ) {
//        CurrentUser currentUser = userRepository.getCurrentUser();
//        if (Objects.isNull(currentUser)) {
//            return ResponseEntity.notFound().build();
//        }
//        Check newCheck = new Check();
//        newCheck.setName(request.getName());
//        newCheck.setDate(request.getDate());
//        newCheck.setUsers(Stream.of(currentUser).collect(Collectors.toList()));
//        newCheck.setPurchases(Collections.emptyList());
//        currentUser.getChecks().add(newCheck);
//        userRepository.saveCurrentUser(currentUser);
//
//        CheckResponse view = conversionService.convert(newCheck, CheckResponse.class);
//        return ResponseEntity.ok(view);
//    }

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
        CurrentUser currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        Check check = currentUser.getChecks().stream()
                .filter(x -> Objects.equals(checkId, x.getId()))
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            return ResponseEntity.notFound().build();
        }
        boolean userAlreadyAdded = check.getUsers().stream()
                .filter(x -> Objects.equals(newUserId, x.getId()))
                .findAny()
                .isPresent();
        if (userAlreadyAdded) {
            CheckResponse view = conversionService.convert(check, CheckResponse.class);
            return ResponseEntity.ok(view);
        }
        User newUser = userRepository.getUser(newUserId);
        if (Objects.isNull(newUser)) {
            return ResponseEntity.notFound().build();
        }
        check.getUsers().add(newUser);
        check.setPurchases(Collections.emptyList());
        userRepository.saveCurrentUser(currentUser);
        CheckResponse view = conversionService.convert(check, CheckResponse.class);
        return ResponseEntity.ok(view);
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
        CurrentUser currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return ResponseEntity.notFound().build();
        }
        Check check = currentUser.getChecks().stream()
                .filter(x -> Objects.equals(checkId, x.getId()))
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            return ResponseEntity.notFound().build();
        }
        Optional<User> userToRemoveOpt = check.getUsers().stream()
                .filter(x -> Objects.equals(removeUserId, x.getId()))
                .findAny();
        if (!userToRemoveOpt.isPresent()) {
            CheckResponse view = conversionService.convert(check, CheckResponse.class);
            return ResponseEntity.ok(view);
        }
        check.getUsers().remove(userToRemoveOpt.get());
        userRepository.saveCurrentUser(currentUser);
        CheckResponse view = conversionService.convert(check, CheckResponse.class);
        return ResponseEntity.ok(view);
    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception ex) {
        LOGGER.error("Error: ", ex);
        return ResponseEntity.internalServerError().build();
    }
}
