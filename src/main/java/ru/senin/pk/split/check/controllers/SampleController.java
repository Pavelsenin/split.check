package ru.senin.pk.split.check.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senin.pk.split.check.data.layer.dao.CheckDao;
import ru.senin.pk.split.check.data.layer.dao.PurchaseDao;
import ru.senin.pk.split.check.data.layer.dao.UserDao;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;
import ru.senin.pk.split.check.entities.Check;
import ru.senin.pk.split.check.entities.Purchase;
import ru.senin.pk.split.check.utils.SerializationUtils;

import java.util.List;
import java.util.Optional;

@RestController
public class SampleController {

    private UserDao userDao;

    private CheckDao checkDao;

    private PurchaseDao purchaseDao;

    @Autowired
    public SampleController(UserDao userDao, CheckDao checkDao, PurchaseDao purchaseDao) {
        this.userDao = userDao;
        this.checkDao = checkDao;
        this.purchaseDao = purchaseDao;
    }

    @GetMapping(path = "/test/sample")
    public String get() {
        return "sample_result";
    }

    @GetMapping(path = "/test/user")
    public String getUser(@RequestParam Long id) {
        Optional<UserEntity> userEntity = userDao.getUser(id);
        return userEntity.map(SerializationUtils::toString).orElse("User not found");
    }

    @GetMapping(path = "/test/user_checks")
    public String getUserChecks(@RequestParam Long id) {
        Optional<UserEntity> userEntity = userDao.getUser(id);
        if (!userEntity.isPresent()) {
            return "User not found";
        }
        List<CheckEntity> checkEntities = checkDao.getChecks(userEntity.get().getChecksIds());
        return SerializationUtils.toString(userEntity.get()) + " " + SerializationUtils.toString(checkEntities);
    }

    @GetMapping(path = "/test/check")
    public String getCheck(@RequestParam Long id) {
        Optional<CheckEntity> checkEntity = checkDao.getCheck(id);
        return checkEntity.map(SerializationUtils::toString).orElse("Check not found");
    }

    @GetMapping(path = "/test/purchase")
    public String getPurchase(@RequestParam Long id) {
        Optional<PurchaseEntity> purchaseEntity = purchaseDao.getPurchase(id);
        return purchaseEntity.map(SerializationUtils::toString).orElse("Purchase not found");
    }

    @ExceptionHandler
    public String handleException(Exception ex) {
        return "Not found";
    }
}
