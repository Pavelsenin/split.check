package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.controllers.responses.TransferResponse;
import ru.senin.pk.split.check.model.Transfer;

@Component
public class TransferToTransferResponseConverter implements Converter<Transfer, TransferResponse> {

    @Override
    public TransferResponse convert(Transfer source) {
        return new TransferResponse(
                source.getPayer().getId(),
                source.getConsumer().getId(),
                source.getAmount()
        );
    }
}
