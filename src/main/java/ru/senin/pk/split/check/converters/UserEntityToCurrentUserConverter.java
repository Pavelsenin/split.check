package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.CurrentUser;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.Collections;

@Component
public class UserEntityToCurrentUserConverter implements Converter<UserEntity, CurrentUser> {

    @Override
    public CurrentUser convert(UserEntity source) {
        return new CurrentUser(
                source.getId(),
                source.getName(),
                Collections.emptyList()
        );
    }
}
