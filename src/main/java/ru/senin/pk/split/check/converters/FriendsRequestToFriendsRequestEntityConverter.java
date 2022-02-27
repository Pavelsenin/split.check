package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.FriendsRequestEntity;
import ru.senin.pk.split.check.model.FriendsRequest;

@Component
public class FriendsRequestToFriendsRequestEntityConverter implements Converter<FriendsRequest, FriendsRequestEntity> {

    @Override
    public FriendsRequestEntity convert(FriendsRequest source) {
        return new FriendsRequestEntity(
                source.getSourceUser().getId(),
                source.getTargetUser().getId(),
                null
        );
    }
}
