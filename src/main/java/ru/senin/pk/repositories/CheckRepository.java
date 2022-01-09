package ru.senin.pk.repositories;

import ru.senin.pk.entities.CheckEntity;

import java.util.List;

public interface CheckRepository {
    List<CheckEntity> getChecksByUserId(String userId);

    CheckEntity saveCheck(CheckEntity checkEntity);
}
