package ru.senin.pk.split.check.data.layer.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.senin.pk.split.check.auth.CurrentUserService;
import ru.senin.pk.split.check.data.layer.dao.*;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;
import ru.senin.pk.split.check.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;

    private final CheckDao checkDao;

    private final PurchaseDao purchaseDao;

    private final UserChecksDao userChecksDao;

    private final ChecksPurchasesDao checksPurchasesDao;

    private final PurchasesPayersDao purchasesPayersDao;

    private final PurchasesConsumersDao purchasesConsumersDao;

    private final CurrentUserService currentUserService;

    private final ConversionService conversionService;

    public UserRepositoryImpl(UserDao userDao, CheckDao checkDao, PurchaseDao purchaseDao, UserChecksDao userChecksDao, ChecksPurchasesDao checksPurchasesDao, PurchasesPayersDao purchasesPayersDao, PurchasesConsumersDao purchasesConsumersDao, CurrentUserService currentUserService, ConversionService conversionService) {
        this.userDao = userDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
        this.userChecksDao = userChecksDao;
        this.checksPurchasesDao = checksPurchasesDao;
        this.purchasesPayersDao = purchasesPayersDao;
        this.purchasesConsumersDao = purchasesConsumersDao;
        this.currentUserService = currentUserService;
        this.conversionService = conversionService;
    }

    @Autowired


    @Override
    public CurrentUser getCurrentUser() {
        Long currentUserId = currentUserService.getCurrentUserId();
        if (Objects.isNull(currentUserId)) {
            return null;
        }
        UserEntity userEntity = userDao.getUserById(currentUserId);
        if (Objects.isNull(userEntity)) {
            return null;
        }
        CurrentUser currentUser = conversionService.convert(userEntity, CurrentUser.class);

        // Receive user checks
        List<Long> currentUserCheckIds = userChecksDao.getChecks(currentUser.getId());
        List<CheckEntity> checkEntities = checkDao.getChecksByIds(currentUserCheckIds);
        List<Check> checks = checkEntities.stream()
                .map(entity -> conversionService.convert(entity, Check.class))
                .collect(Collectors.toList());
        currentUser.setChecks(checks);
        //

        for (Check check : checks) {
            // Receive check users
            List<Long> checkUserIds = userChecksDao.getUsers(check.getId());
            List<UserEntity> checkUserEntities = userDao.getUsersByIds(checkUserIds);
            List<User> checkUsers = checkUserEntities.stream()
                    .map(entity -> conversionService.convert(entity, RegisteredUser.class))
                    .collect(Collectors.toList());
            check.setUsers(checkUsers);
            //

            // Receive check purchases
            List<Long> checkPurchaseIds = checksPurchasesDao.getPurchases(check.getId());
            List<PurchaseEntity> purchaseEntities = purchaseDao.getPurchasesByIds(checkPurchaseIds);
            List<Purchase> purchases = purchaseEntities.stream()
                    .map(entity -> conversionService.convert(entity, Purchase.class))
                    .collect(Collectors.toList());
            check.setPurchases(purchases);
            //

            for (Purchase purchase : purchases) {
                // Receive payer
                Long payerUserId = purchasesPayersDao.getPayerUser(purchase.getId());
                UserEntity purchasePayerEntity = userDao.getUserById(payerUserId);
                User payerUser = conversionService.convert(purchasePayerEntity, RegisteredUser.class);
                purchase.setPayer(payerUser);
                //

                // Receive consumers
                List<Long> consumerUserIds = purchasesConsumersDao.getConsumerUsers(purchase.getId());
                List<UserEntity> purchaseConsumerEntities = userDao.getUsersByIds(consumerUserIds);
                List<User> purchaseConsumers = purchaseConsumerEntities.stream()
                        .map(entity -> conversionService.convert(entity, RegisteredUser.class))
                        .collect(Collectors.toList());
                purchase.setConsumers(purchaseConsumers);
                //
            }
        }

        return currentUser;
    }

    @Override
    public User getUser(Long userId) {
        if (Objects.isNull(userId)) {
            return null;
        }
        UserEntity userEntity = userDao.getUserById(userId);
        if (Objects.isNull(userEntity)) {
            return null;
        }
        RegisteredUser user = conversionService.convert(userEntity, RegisteredUser.class);
        return user;
    }

    @Transactional
    @Override
    public void saveCurrentUser(CurrentUser user) {
        UserEntity userEntity = conversionService.convert(user, UserEntity.class);
        userDao.saveUser(userEntity);
        for (Check check : user.getChecks()) {
            CheckEntity checkEntity = conversionService.convert(check, CheckEntity.class);
            checkDao.saveCheck(checkEntity);
            check.setId(checkEntity.getId());

            List<Long> usersIds = check.getUsers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            userChecksDao.setUsers(usersIds, check.getId());

            for (Purchase purchase : check.getPurchases()) {
                PurchaseEntity purchaseEntity = conversionService.convert(purchase, PurchaseEntity.class);
                purchaseDao.savePurchase(purchaseEntity);
                purchase.setId(purchaseEntity.getId());

                purchasesPayersDao.setPayerUser(purchase.getPayer().getId(), purchase.getId());

                List<Long> consumerIds = purchase.getConsumers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList());
                purchasesConsumersDao.setConsumerUsers(consumerIds, purchase.getId());
            }

            List<Long> purchaseIds = check.getPurchases().stream()
                    .map(Purchase::getId)
                    .collect(Collectors.toList());
            checksPurchasesDao.setPurchases(purchaseIds, checkEntity.getId());

        }

        List<Long> checkIds = user.getChecks().stream()
                .map(Check::getId)
                .collect(Collectors.toList());
        userChecksDao.setChecks(checkIds, userEntity.getId());
    }
}
