package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.controllers.responses.UserResponse;

@Component
public class UserToUserResponseConverter implements Converter<User, UserResponse> {

    @Override
    public UserResponse convert(User source) {
        return new UserResponse(
                source.getId(),
                source.getName()
        );
    }
}
