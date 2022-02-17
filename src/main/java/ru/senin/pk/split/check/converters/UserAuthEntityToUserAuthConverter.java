package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.UserAuthEntity;
import ru.senin.pk.split.check.model.UserAuth;

@Component
public class UserAuthEntityToUserAuthConverter implements Converter<UserAuthEntity, UserAuth> {

    @Override
    public UserAuth convert(UserAuthEntity source) {
        return new UserAuth(
                source.getUsername(),
                source.getPassword()
        );
    }
}
