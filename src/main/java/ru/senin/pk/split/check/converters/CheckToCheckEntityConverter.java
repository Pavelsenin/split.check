package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.utils.DateUtils;

@Component
public class CheckToCheckEntityConverter implements Converter<Check, CheckEntity> {

    @Override
    public CheckEntity convert(Check source) {
        return new CheckEntity(
                source.getId(),
                source.getName(),
                DateUtils.asDate(source.getDate())
        );
    }
}
