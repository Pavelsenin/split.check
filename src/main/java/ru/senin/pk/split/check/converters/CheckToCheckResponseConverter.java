package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.controllers.responses.CheckResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CheckToCheckResponseConverter implements Converter<Check, CheckResponse> {

    @Override
    public CheckResponse convert(Check source) {
        List<Long> purchaseIds = source.getPurchases().stream()
                .map(Purchase::getId)
                .collect(Collectors.toList());
        List<Long> userIds = source.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return new CheckResponse(
                source.getId(),
                source.getName(),
                source.getDate(),
                purchaseIds,
                userIds
        );
    }
}
