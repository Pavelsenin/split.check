package ru.senin.pk.split.check.integration.test.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
public enum ITParam {
    ORIGIN(String.class),
    COOKIES(String.class),
    GENERATED_CHECK_NAME(String.class),
    GENERATED_CHECK_DATE(String.class),
    GENERATED_PURCHASE_NAME(String.class),
    USERNAME(String.class),
    PASSWORD(String.class),
    USER_ID(String.class),
    FRIEND_USER_ID(String.class),
    PAYER_ID(String.class),
    CONSUMERS_IDS(List.class),
    CHECK_ID(String.class),
    PURCHASE_COST(String.class);

    @Getter
    private Class clazz;
}
