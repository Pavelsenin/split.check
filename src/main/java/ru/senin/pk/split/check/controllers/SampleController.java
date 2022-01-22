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
import ru.senin.pk.split.check.utils.SerializationUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class SampleController {

    private final UserDao userDao;

    private final CheckDao checkDao;

    private final PurchaseDao purchaseDao;

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
        Optional<UserEntity> userEntity = userDao.getUserById(id);
        return userEntity.map(SerializationUtils::toString).orElse("User not found");
    }

    @GetMapping(path = "/test/user_checks")
    public String getUserChecks(@RequestParam Long id) {
        Optional<UserEntity> userEntity = userDao.getUserById(id);
        if (!userEntity.isPresent()) {
            return "User not found";
        }
        checkDao.getChecksByIds(null);
        List<CheckEntity> checkEntities = checkDao.getChecksByIds(userEntity.get().getChecksIds());
        return SerializationUtils.toString(userEntity.get()) + " " + SerializationUtils.toString(checkEntities);
    }

    @GetMapping(path = "/test/check/get")
    public String getCheck(@RequestParam Long id) {
        Optional<CheckEntity> checkEntity = checkDao.getCheckById(id);
        return checkEntity.map(SerializationUtils::toString).orElse("Check not found");
    }

    @GetMapping(path = "/test/check/create")
    public String createCheck() {
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setName("new_name");
        checkEntity.setDate(new Date());
        checkDao.saveCheck(checkEntity);
        return SerializationUtils.toString(checkEntity);
    }

    @GetMapping(path = "/test/check/update/add_user")
    public String updateCheckAddUser(@RequestParam("check_id") Long checkId, @RequestParam("user_id") Long userId) {
        Optional<CheckEntity> checkEntityOpt = checkDao.getCheckById(checkId);
        if (!checkEntityOpt.isPresent()) {
            return "Check not found";
        }
        CheckEntity checkEntity = checkEntityOpt.get();
        checkEntity.getUserIds().add(userId);
        checkDao.saveCheck(checkEntity);

        return SerializationUtils.toString(checkEntity);
    }

    @GetMapping(path = "/test/check/update/remove_user")
    public String updateCheckRemoveUser(@RequestParam("check_id") Long checkId, @RequestParam("user_id") Long userId) {
        Optional<CheckEntity> checkEntityOpt = checkDao.getCheckById(checkId);
        if (!checkEntityOpt.isPresent()) {
            return "Check not found";
        }
        CheckEntity checkEntity = checkEntityOpt.get();
        checkEntity.getUserIds().remove(userId);
        checkDao.saveCheck(checkEntity);

        return SerializationUtils.toString(checkEntity);
    }

    @GetMapping(path = "/test/purchase")
    public String getPurchase(@RequestParam Long id) {
        Optional<PurchaseEntity> purchaseEntity = purchaseDao.getPurchaseById(id);
        return purchaseEntity.map(SerializationUtils::toString).orElse("Purchase not found");
    }

    @ExceptionHandler
    public String handleException(Exception ex) {
        return "Not found";
    }
}
