package ru.senin.pk.split.check.integration.test.utils;

import java.util.HashMap;
import java.util.Objects;

public class ITParams extends HashMap<String, Object> {

    public ITParams() {

    }

    public ITParams(ITParams o) {
        putAll(o);
    }

    public <T> T put(ITParam param, T value) {
        if (!param.getClazz().isAssignableFrom(value.getClass())) {
            throw new ClassCastException("Invalid value class. param: " + param + ", value " + value);
        }
        return (T) put(param.name(), value);
    }

    public <T> T get(ITParam param) {
        Object rawValue = get(param.name());
        if (Objects.isNull(rawValue)) {
            return null;
        }
        try {
            return (T) rawValue;
        } catch (ClassCastException e) {
            throw new ClassCastException("Invalid value class. param: " + param + ", value " + rawValue);
        }
    }
}
