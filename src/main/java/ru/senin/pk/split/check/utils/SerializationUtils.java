package ru.senin.pk.split.check.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
