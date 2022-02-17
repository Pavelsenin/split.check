package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.UserAuthEntity;
import ru.senin.pk.split.check.model.UserAuth;

@Component
public class UserAuthToUserAuthEntityConverter implements Converter<UserAuth, UserAuthEntity> {

    @Override
    public UserAuthEntity convert(UserAuth source) {
        return new UserAuthEntity(
                source.getUsername(),
                source.getPassword(),
                null
        );
    }
}
