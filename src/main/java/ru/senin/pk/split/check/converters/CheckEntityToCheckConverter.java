package ru.senin.pk.split.check.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.model.Check;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;
import ru.senin.pk.split.check.utils.DateUtils;

import java.util.Collections;

@Component
public class CheckEntityToCheckConverter implements Converter<CheckEntity, Check> {

    @Override
    public Check convert(CheckEntity source) {
        return new Check(
                source.getId(),
                source.getName(),
                DateUtils.asLocalDate(source.getDate()),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
