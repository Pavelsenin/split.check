package ru.senin.pk.split.check.data.layer.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.senin.pk.split.check.data.layer.dao.*;
import ru.senin.pk.split.check.data.layer.entities.*;
import ru.senin.pk.split.check.model.*;
import ru.senin.pk.split.check.utils.SerializationUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;

    private final UserAuthDao userAuthDao;

    private final CheckDao checkDao;

    private final PurchaseDao purchaseDao;

    private final UserChecksDao userChecksDao;

    private final ChecksPurchasesDao checksPurchasesDao;

    private final PurchasesPayersDao purchasesPayersDao;

    private final PurchasesConsumersDao purchasesConsumersDao;

    private final FriendsRequestsDao friendsRequestsDao;

    private final ConversionService conversionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    public UserRepositoryImpl(UserDao userDao, UserAuthDao userAuthDao, CheckDao checkDao, PurchaseDao purchaseDao, UserChecksDao userChecksDao, ChecksPurchasesDao checksPurchasesDao, PurchasesPayersDao purchasesPayersDao, PurchasesConsumersDao purchasesConsumersDao, FriendsRequestsDao friendsRequestsDao, ConversionService conversionService) {
        this.userDao = userDao;
        this.userAuthDao = userAuthDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
        this.userChecksDao = userChecksDao;
        this.checksPurchasesDao = checksPurchasesDao;
        this.purchasesPayersDao = purchasesPayersDao;
        this.purchasesConsumersDao = purchasesConsumersDao;
        this.friendsRequestsDao = friendsRequestsDao;
        this.conversionService = conversionService;
    }

    @Override
    public CurrentUser getCurrentUserByUsername(String username) {
        LOGGER.info("Get current user by username. username: {}", username);
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByUsername(username);
        if (Objects.isNull(userAuthEntity)) {
            LOGGER.info("Current user not found by username. username: {}", username);
            return null;
        }
        UserAuth userAuth = conversionService.convert(userAuthEntity, UserAuth.class);
        CurrentUser currentUser = getCurrentUserById(userAuthEntity.getUserId());
        currentUser.setAuth(userAuth);
        LOGGER.info("Current user found by username. currentUser: {}", currentUser);
        return currentUser;
    }

    private CurrentUser getCurrentUserById(Long id) {
        LOGGER.info("Get current user by id. id: {}", id);
        UserEntity userEntity = userDao.getUserById(id);
        if (Objects.isNull(userEntity)) {
            LOGGER.info("Current user not found by id. id: {}", id);
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
        //

        // Receive friend requests
        currentUser.setUserFriendRequests(getUserFriendRequests(currentUser));
        LOGGER.info("Current user found by id. currentUser: {}", currentUser);
        return currentUser;
    }

    private UserFriendRequests getUserFriendRequests(CurrentUser currentUser) {
        UserFriendRequests userFriendRequests = new UserFriendRequests();
        List<FriendsRequestEntity> friendRequestEntities = friendsRequestsDao.getFriendsRequests(currentUser.getId());
        for (FriendsRequestEntity friendsRequestEntity : friendRequestEntities) {
            if (Objects.equals(friendsRequestEntity.getStatus(), FriendsRequestEntity.Status.DECLINED)) {
                continue;
            }
            FriendsRequest friendsRequest = conversionService.convert(friendsRequestEntity, FriendsRequest.class);
            UserEntity sourceUserEntity = userDao.getUserById(friendsRequestEntity.getSourceUserId());
            RegisteredUser sourceUser = conversionService.convert(sourceUserEntity, RegisteredUser.class);
            friendsRequest.setSourceUser(sourceUser);
            UserEntity targetUserEntity = userDao.getUserById(friendsRequestEntity.getTargetUserId());
            RegisteredUser targetUser = conversionService.convert(targetUserEntity, RegisteredUser.class);
            friendsRequest.setTargetUser(targetUser);
            if (Objects.equals(friendsRequestEntity.getStatus(), FriendsRequestEntity.Status.PENDING)) {
                if (Objects.equals(friendsRequest.getSourceUser().getId(), currentUser.getId())) {
                    userFriendRequests.getOutgoingFriendsRequests().add(friendsRequest);
                } else {
                    userFriendRequests.getIncomingFriendsRequests().add(friendsRequest);
                }
            } else if (Objects.equals(friendsRequestEntity.getStatus(), FriendsRequestEntity.Status.ACCEPTED)) {
                userFriendRequests.getAcceptedFriendsRequests().add(friendsRequest);
            }
        }
        return userFriendRequests;
    }

    @Transactional
    @Override
    public void saveCurrentUser(CurrentUser currentUser) {
        LOGGER.info("Save current user. currentUser: {}", SerializationUtils.toString(currentUser));
        UserEntity userEntity = conversionService.convert(currentUser, UserEntity.class);
        userDao.saveUser(userEntity);
        currentUser.setId(userEntity.getId());
        for (Check check : currentUser.getChecks()) {
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

        List<Long> checkIds = currentUser.getChecks().stream()
                .map(Check::getId)
                .collect(Collectors.toList());
        userChecksDao.setChecks(checkIds, userEntity.getId());

        saveUserFriendRequests(currentUser);

        UserAuth auth = currentUser.getAuth();
        UserAuthEntity authEntity = conversionService.convert(auth, UserAuthEntity.class);
        authEntity.setUserId(userEntity.getId());
        userAuthDao.saveUserAuth(authEntity);
        LOGGER.info("Current user saved. currentUser: {}", SerializationUtils.toString(currentUser));
    }

    private void saveUserFriendRequests(CurrentUser currentUser) {
        UserFriendRequests userFriendRequests = currentUser.getUserFriendRequests();
        List<FriendsRequestEntity> friendRequestEntities = new ArrayList<>();
        for (FriendsRequest acceptedFriendsRequest : userFriendRequests.getAcceptedFriendsRequests()) {
            FriendsRequestEntity friendsRequestEntity = conversionService.convert(acceptedFriendsRequest, FriendsRequestEntity.class);
            friendsRequestEntity.setStatus(FriendsRequestEntity.Status.ACCEPTED);
            friendRequestEntities.add(friendsRequestEntity);
        }
        for (FriendsRequest incomingFriendsRequest : userFriendRequests.getIncomingFriendsRequests()) {
            FriendsRequestEntity friendsRequestEntity = conversionService.convert(incomingFriendsRequest, FriendsRequestEntity.class);
            friendsRequestEntity.setStatus(FriendsRequestEntity.Status.PENDING);
            friendRequestEntities.add(friendsRequestEntity);
        }
        for (FriendsRequest outgoingFriendsRequest : userFriendRequests.getOutgoingFriendsRequests()) {
            FriendsRequestEntity friendsRequestEntity = conversionService.convert(outgoingFriendsRequest, FriendsRequestEntity.class);
            friendsRequestEntity.setStatus(FriendsRequestEntity.Status.PENDING);
            friendRequestEntities.add(friendsRequestEntity);
        }
        friendsRequestsDao.saveFriendsRequests(currentUser.getId(), friendRequestEntities);
    }

    @Override
    public RegisteredUser getRegisteredUserById(Long id) {
        LOGGER.info("Get registered user by id. id: {}", id);
        if (Objects.isNull(id)) {
            return null;
        }
        UserEntity userEntity = userDao.getUserById(id);
        if (Objects.isNull(userEntity)) {
            LOGGER.info("Registered user not found by id. id: {}", id);
            return null;
        }
        RegisteredUser user = conversionService.convert(userEntity, RegisteredUser.class);
        LOGGER.info("Registered user  found by id. user: {}", user);
        return user;
    }
}
