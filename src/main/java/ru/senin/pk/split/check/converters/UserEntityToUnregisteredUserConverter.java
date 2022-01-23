package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.UnregisteredUser;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

@Component
public class UserEntityToUnregisteredUserConverter implements Converter<UserEntity, UnregisteredUser> {

    @Override
    public UnregisteredUser convert(UserEntity source) {
        return new UnregisteredUser(
                source.getId(),
                source.getName()
        );
    }
}
