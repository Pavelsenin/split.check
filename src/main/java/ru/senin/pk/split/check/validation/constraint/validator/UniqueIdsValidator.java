package ru.senin.pk.split.check.validation.constraint.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Objects;

/**
 * Unique collection elements validator
 */
public class UniqueIdsValidator implements ConstraintValidator<UniqueIds, Collection<Long>> {

    @Override
    public boolean isValid(Collection<Long> value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        return value.stream().distinct().count() == value.size();
    }

    @Override
    public void initialize(UniqueIds constraintAnnotation) {

    }
}
