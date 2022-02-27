package ru.senin.pk.split.check.integration.test.utils;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ITJsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITJsonUtils.class);

    public static String getValue(String body, String jsonPath) {
        LOGGER.debug("Get value. body: {}, jsonPath: {}", body, jsonPath);
        String value;
        try {
            value = JsonPath.parse(body).read(jsonPath, String.class);
        } catch (Exception e) {
            LOGGER.debug("Value not received", e);
            return StringUtils.EMPTY;
        }
        LOGGER.debug("Value received. value: {}", value);
        return value;
    }
}
