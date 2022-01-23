package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.Purchase;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;

@Component
public class PurchaseToPurchaseEntityConverter implements Converter<Purchase, PurchaseEntity> {

    @Override
    public PurchaseEntity convert(Purchase source) {
        return new PurchaseEntity(
                source.getId(),
                source.getName(),
                source.getCost()
        );
    }
}
