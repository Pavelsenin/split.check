package ru.senin.pk.split.check.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.controllers.responses.TransferResponse;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.model.User;
import ru.senin.pk.split.check.controllers.responses.GetCheckResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CheckToGetCheckResponseConverter implements Converter<Check, GetCheckResponse> {

    private final ConversionService conversionService;

    @Autowired
    public CheckToGetCheckResponseConverter(@Lazy ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public GetCheckResponse convert(Check source) {
        List<Long> purchaseIds = source.getPurchases().stream()
                .map(Purchase::getId)
                .collect(Collectors.toList());
        List<Long> userIds = source.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        List<TransferResponse> checkTransfers = source.getTransfers().stream()
                .map(transfer -> conversionService.convert(transfer, TransferResponse.class))
                .collect(Collectors.toList());
        return new GetCheckResponse(
                source.getId(),
                source.getName(),
                source.getDate(),
                purchaseIds,
                userIds,
                checkTransfers
        );
    }
}
