package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.FriendsRequestEntity;
import ru.senin.pk.split.check.model.FriendsRequest;

@Component
public class FriendsRequestEntityToFriendsRequestConverter implements Converter<FriendsRequestEntity, FriendsRequest> {

    @Override
    public FriendsRequest convert(FriendsRequestEntity source) {
        return new FriendsRequest(
                null,
                null
        );
    }
}
