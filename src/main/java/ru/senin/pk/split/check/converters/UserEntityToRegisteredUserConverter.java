package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.RegisteredUser;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

@Component
public class UserEntityToRegisteredUserConverter implements Converter<UserEntity, RegisteredUser> {

    @Override
    public RegisteredUser convert(UserEntity source) {
        return new RegisteredUser(
                source.getId(),
                source.getName()
        );
    }
}
