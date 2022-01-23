package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.controllers.requests.AddNewPurchaseRequest;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.utils.SerializationUtils;
import ru.senin.pk.split.check.controllers.responses.PurchaseResponse;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/purchases", produces = "application/json")
@CrossOrigin(origins = "*")
public class PurchasesController {

    private final UserRepository userRepository;

    private final ConversionService conversionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchasesController.class);

    @Autowired
    public PurchasesController(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    @GetMapping(path = "/get")
    @ResponseBody
    public ResponseEntity getPurchases(
            @RequestParam("check_id") Long checkId
    ) {
        LOGGER.info("Get purchases. checkId: {}", checkId);
        CurrentUser currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            LOGGER.info("Current user not found");
            return ResponseEntity.notFound().build();
        }
        Check check = currentUser.getChecks().stream()
                .filter(x -> Objects.equals(x.getId(), checkId))
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Check not found for current user. check id: {}, user: {}", checkId, SerializationUtils.toString(currentUser));
            }
            return ResponseEntity.notFound().build();
        }
        List<Purchase> purchases = check.getPurchases();
        List<PurchaseResponse> response = purchases.stream()
                .map(purchase -> conversionService.convert(purchase, PurchaseResponse.class))
                .collect(Collectors.toList());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Purchases found. purchases: {}, response: {}", SerializationUtils.toString(purchases), SerializationUtils.toString(response));
        }
        return ResponseEntity.ok(response);
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
    public ResponseEntity addNewPurchase(
            @RequestParam("check_id") Long checkId,
            @RequestBody AddNewPurchaseRequest request
    ) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Add new purchase. checkId: {}, request: {}", checkId, SerializationUtils.toString(request));
        }
        CurrentUser currentUser = userRepository.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            LOGGER.info("Current user not found");
            return ResponseEntity.notFound().build();
        }
        Check check = currentUser.getChecks().stream()
                .filter(x -> Objects.equals(x.getId(), checkId))
                .findAny()
                .orElse(null);
        if (Objects.isNull(check)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Check not found for current user. check id: {}, user: {}", checkId, SerializationUtils.toString(currentUser));
            }
            return ResponseEntity.notFound().build();
        }
        Purchase newPurchase = new Purchase();
        newPurchase.setName(request.getName());
        newPurchase.setCost(request.getCost());
        Long payerId = request.getPayer();
        User payer = findUser(check.getUsers(), payerId);
        if (Objects.isNull(payer)) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Payer not found in check. payer id: {}, check: {}", payerId, SerializationUtils.toString(check));
            }
            return ResponseEntity.notFound().build();
        }
        newPurchase.setPayer(payer);
        List<User> consumers = new ArrayList<>();
        for (Long consumerId: request.getConsumers()) {
            User consumer = findUser(check.getUsers(), request.getPayer());
            if (Objects.isNull(payer)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Consumer not found in check. consumer id: {}, check: {}", consumerId, SerializationUtils.toString(check));
                }
                return ResponseEntity.notFound().build();
            }
            consumers.add(consumer);
        }
        newPurchase.setConsumers(consumers);
        check.getPurchases().add(newPurchase);
        userRepository.saveCurrentUser(currentUser);
        User x = userRepository.getCurrentUser();
        PurchaseResponse response = conversionService.convert(newPurchase, PurchaseResponse.class);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("New purchase added. newPurchase: {}, response: {} ", SerializationUtils.toString(newPurchase), SerializationUtils.toString(response));
        }
        return ResponseEntity.ok(response);
    }

    private User findUser(List<User> users, Long userId) {
        return users.stream()
                .filter(x -> Objects.equals(x.getId(), userId))
                .findAny()
                .orElse(null);
    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception ex) {
        LOGGER.error("Error: ", ex);
        return ResponseEntity.internalServerError().build();
    }
}
