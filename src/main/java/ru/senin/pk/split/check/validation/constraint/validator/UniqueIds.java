package ru.senin.pk.split.check.validation.constraint.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * The annotated collection must contain only unique elements
 */
@Documented
@Constraint(validatedBy = UniqueIdsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIds {
    String message() default "Collection has non unique values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
