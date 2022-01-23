package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;

import java.util.Collections;

@Component
public class PurchaseEntityToPurchaseConverter implements Converter<PurchaseEntity, Purchase> {

    @Override
    public Purchase convert(PurchaseEntity source) {
        return new Purchase(
                source.getId(),
                source.getName(),
                source.getCost(),
                null,
                Collections.emptyList()
        );
    }
}
