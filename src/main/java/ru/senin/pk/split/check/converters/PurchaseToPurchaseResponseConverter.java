package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.controllers.responses.GetPurchasesResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseToPurchaseResponseConverter implements Converter<Purchase, GetPurchasesResponse> {

    @Override
    public GetPurchasesResponse convert(Purchase source) {
        List<Long> consumerIds = source.getConsumers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return new GetPurchasesResponse(
                source.getId(),
                source.getName(),
                source.getCost(),
                source.getPayer().getId(),
                consumerIds
        );
    }
}
