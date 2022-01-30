package ru.senin.pk.split.check.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.senin.pk.split.check.controllers.requests.AddNewPurchaseRequest;
import ru.senin.pk.split.check.data.layer.repositories.UserRepository;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.controllers.responses.PurchaseResponse;
import ru.senin.pk.split.check.validation.ValidatedAccess;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/purchases", produces = "application/json")
@CrossOrigin(origins = "*")
@Validated
public class PurchasesController {

    private final UserRepository userRepository;

    private final ConversionService conversionService;

    private final ValidatedAccess validatedAccess;

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchasesController.class);

    @Autowired
    public PurchasesController(UserRepository userRepository, ConversionService conversionService, ValidatedAccess validatedAccess) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        this.validatedAccess = validatedAccess;
    }

    @GetMapping(path = "/get")
    @ResponseBody
    public ResponseEntity getPurchases(
            @RequestParam("check_id") Long checkId
    ) {
        LOGGER.info("Get purchases. checkId: {}", checkId);
        CurrentUser currentUser = userRepository.getCurrentUser();
        validatedAccess.validateCurrentUser(currentUser);
        Check check = validatedAccess.getCurrentUserCheck(currentUser, checkId);
        List<Purchase> purchases = check.getPurchases();
        List<PurchaseResponse> response = purchases.stream()
                .map(purchase -> conversionService.convert(purchase, PurchaseResponse.class))
                .collect(Collectors.toList());
        LOGGER.info("Purchases found. purchases: {}, response: {}", purchases, response);
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
            @RequestParam("check_id") @NotNull Long checkId,
            @RequestBody @Valid AddNewPurchaseRequest request
    ) {
        LOGGER.info("Add new purchase. checkId: {}, request: {}", checkId, request);
        CurrentUser currentUser = userRepository.getCurrentUser();
        validatedAccess.validateCurrentUser(currentUser);
        Check check = validatedAccess.getCurrentUserCheck(currentUser, checkId);

        Purchase newPurchase = new Purchase();
        newPurchase.setName(request.getName());
        newPurchase.setCost(request.getCost());

        User payer = validatedAccess.getCheckUser(check, request.getPayer());
        newPurchase.setPayer(payer);

        List<User> consumers = request.getConsumers().stream()
                .map(consumerId -> validatedAccess.getCheckUser(check, consumerId))
                .collect(Collectors.toList());
        newPurchase.setConsumers(consumers);

        check.getPurchases().add(newPurchase);

        userRepository.saveCurrentUser(currentUser);
        PurchaseResponse response = conversionService.convert(newPurchase, PurchaseResponse.class);
        LOGGER.info("New purchase added. newPurchase: {}, response: {} ", newPurchase, response);
        return ResponseEntity.ok(response);
    }
}
