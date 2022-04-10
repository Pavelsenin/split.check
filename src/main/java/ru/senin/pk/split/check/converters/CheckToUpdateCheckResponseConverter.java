package ru.senin.pk.split.check.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.controllers.responses.TransferResponse;
import ru.senin.pk.split.check.controllers.responses.UpdateCheckResponse;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CheckToUpdateCheckResponseConverter implements Converter<Check, UpdateCheckResponse> {

    private final ConversionService conversionService;

    @Autowired
    public CheckToUpdateCheckResponseConverter(@Lazy ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public UpdateCheckResponse convert(Check source) {
        List<Long> purchaseIds = source.getPurchases().stream()
                .map(Purchase::getId)
                .collect(Collectors.toList());
        List<Long> userIds = source.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        List<TransferResponse> checkTransfers = source.getTransfers().stream()
                .map(transfer -> conversionService.convert(transfer, TransferResponse.class))
                .collect(Collectors.toList());
        return new UpdateCheckResponse(
                source.getId(),
                source.getName(),
                source.getDate(),
                purchaseIds,
                userIds,
                checkTransfers
        );
    }
}
