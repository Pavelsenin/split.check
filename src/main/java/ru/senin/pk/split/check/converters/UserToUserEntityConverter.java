package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

@Component
public class UserToUserEntityConverter implements Converter<User, UserEntity> {

    @Override
    public UserEntity convert(User source) {
        return new UserEntity(
                source.getId(),
                source.getName()
        );
    }
}
