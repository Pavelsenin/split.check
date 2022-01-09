package ru.senin.pk.repositories;

import ru.senin.pk.entities.CheckEntity;
import ru.senin.pk.entities.UserEntity;

import java.util.List;

public interface UserRepository {
    UserEntity getUser(String id);
}
