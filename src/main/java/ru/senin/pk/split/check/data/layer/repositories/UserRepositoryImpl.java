package ru.senin.pk.split.check.data.layer.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.senin.pk.split.check.auth.CurrentUserService;
import ru.senin.pk.split.check.data.layer.dao.*;
import ru.senin.pk.split.check.data.layer.dto.CheckDto;
import ru.senin.pk.split.check.data.layer.dto.PurchaseDto;
import ru.senin.pk.split.check.data.layer.dto.UserDto;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository{

    private UserDao userDao;

    private CheckDao checkDao;

    private PurchaseDao purchaseDao;

    private UserChecksDao userChecksDao;

    private ChecksPurchasesDao checksPurchasesDao;

    private CurrentUserService currentUserService;

    public UserRepositoryImpl(UserDao userDao, CheckDao checkDao, PurchaseDao purchaseDao, UserChecksDao userChecksDao, ChecksPurchasesDao checksPurchasesDao, CurrentUserService currentUserService) {
        this.userDao = userDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
        this.userChecksDao = userChecksDao;
        this.checksPurchasesDao = checksPurchasesDao;
        this.currentUserService = currentUserService;
    }

    @Autowired


    @Override
    public UserDto getCurrentUser() {
        Long currentUserId = currentUserService.getCurrentUserId();
        if (Objects.isNull(currentUserId)) {
            return null;
        }
        return getUser(currentUserId);
    }

    public UserDto getUser(Long userId) {
        Optional<UserEntity> userEntityOpt = userDao.getUserById(userId);
        if (!userEntityOpt.isPresent()) {
            return null;
        }
        UserEntity userEntity = userEntityOpt.get();
        UserDto currentUser = convert(userEntity);

        List<CheckEntity> checkEntities = checkDao.getChecksByUserId(currentUser.getId());
        List<CheckDto> checks = checkEntities.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        currentUser.setChecks(checks);

        for (CheckDto check : checks) {
            List<UserEntity> checkUserEntities = userDao.getUsersByCheckId(check.getId());
            List<UserDto> checkUsers = checkUserEntities.stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
            check.setUsers(checkUsers);

            List<PurchaseEntity> purchaseEntities = purchaseDao.getPurchasesByCheckId(check.getId());
            List<PurchaseDto> purchases = purchaseEntities.stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
            check.setPurchases(purchases);

            for (PurchaseDto purchase: purchases) {
                UserEntity purchasePayerEntity = userDao.getPayerByPurchaseId(purchase.getId());
                UserDto payerUser = convert(purchasePayerEntity);
                purchase.setPayer(payerUser);

                List<UserEntity> purchaseConsumerEntities = userDao.getConsumersByPurchaseId(purchase.getId());
                List<UserDto> purchaseConsumers = purchaseConsumerEntities.stream()
                        .map(this::convert)
                        .collect(Collectors.toList());
                purchase.setConsumers(purchaseConsumers);
            }
        }

        return currentUser;
    }

    @Transactional
    @Override
    public void saveUser(UserDto user) {
        UserEntity userEntity = convert(user);
        userDao.saveUser(userEntity);
        for (CheckDto check: user.getChecks()) {
            CheckEntity checkEntity = convert(check);
            checkDao.saveCheck(checkEntity);
            check.setId(checkEntity.getId());

            for (PurchaseDto purchase: check.getPurchases()) {
                PurchaseEntity purchaseEntity = convert(purchase);
                purchaseEntity.setPayerId(purchase.getPayer().getId());
                List<Long> consumerIds = purchase.getConsumers().stream()
                        .map(UserDto::getId)
                        .collect(Collectors.toList());
                purchaseEntity.setConsumerIds(consumerIds);
                purchaseDao.savePurchase(purchaseEntity);
            }

            List<Long> purchaseIds = check.getPurchases().stream()
                    .map(PurchaseDto::getId)
                    .collect(Collectors.toList());
            checksPurchasesDao.unlinkPurchasesFromCheck(checkEntity.getId());
            checksPurchasesDao.linkPurchasesToCheck(purchaseIds, checkEntity.getId());

            List<Long> usersIds = check.getUsers().stream()
                    .map(UserDto::getId)
                    .collect(Collectors.toList());
            checkEntity.setUserIds(usersIds);
        }

        List<Long> checkIds = user.getChecks().stream()
                .map(CheckDto::getId)
                .collect(Collectors.toList());
        userChecksDao.unlinkChecksFromUser(userEntity.getId());
        userChecksDao.linkChecksToUser(checkIds, userEntity.getId());
    }

    private UserDto convert(UserEntity entity) {
        return new UserDto(
                entity.getId(),
                entity.getName(),
                Collections.emptyList()
        );
    }

    private CheckDto convert(CheckEntity entity) {
        return new CheckDto(
                entity.getId(),
                entity.getName(),
                entity.getDate(),
                Collections.emptyList(),
                Collections.emptyList()

        );
    }

    private PurchaseDto convert(PurchaseEntity entity) {
        return new PurchaseDto(
                entity.getId(),
                entity.getName(),
                entity.getCost(),
                null,
                Collections.emptyList()
        );
    }

    private UserEntity convert(UserDto dto) {
        List<Long> checksIds = dto.getChecks().stream()
                .map(CheckDto::getId)
                .collect(Collectors.toList());
        return new UserEntity(
                dto.getId(),
                dto.getName(),
                checksIds
        );
    }

    private CheckEntity convert(CheckDto dto) {
        return new CheckEntity(
                dto.getId(),
                dto.getName(),
                dto.getDate(),
                null,
                null
        );
    }

    private PurchaseEntity convert(PurchaseDto dto) {
        return new PurchaseEntity(
                dto.getId(),
                dto.getName(),
                dto.getCost(),
                null,
                null
        );
    }


}
