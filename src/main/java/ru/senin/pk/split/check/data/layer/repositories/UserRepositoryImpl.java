package ru.senin.pk.split.check.data.layer.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.dao.CheckDao;
import ru.senin.pk.split.check.data.layer.dao.PurchaseDao;
import ru.senin.pk.split.check.data.layer.dao.UserDao;
import ru.senin.pk.split.check.data.layer.dto.CheckDto;
import ru.senin.pk.split.check.data.layer.dto.PurchaseDto;
import ru.senin.pk.split.check.data.layer.dto.UserDto;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository{

    private UserDao userDao;

    private CheckDao checkDao;

    private PurchaseDao purchaseDao;

    @Autowired
    public UserRepositoryImpl(UserDao userDao, CheckDao checkDao, PurchaseDao purchaseDao) {
        this.userDao = userDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
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
}
