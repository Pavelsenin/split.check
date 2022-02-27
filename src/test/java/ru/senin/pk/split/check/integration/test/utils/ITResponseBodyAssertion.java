package ru.senin.pk.split.check.integration.test.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.Validate;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * Integration tests http response body assertion
 * Uses JSONPath to determine checked field
 */
public class ITResponseBodyAssertion implements ITResponseAssertion {

    private String jsonPath;

    private String expectedValue;

    private ITResponseBodyAssertion(String jsonPath, String expectedValue) {
        this.jsonPath = jsonPath;
        this.expectedValue = expectedValue;
    }

    public static ITResponseBodyAssertion assertBody(String jsonPath, String expectedValue) {
        Validate.notBlank(jsonPath);
        Validate.notNull(expectedValue);
        return new ITResponseBodyAssertion(jsonPath, expectedValue);
    }

    @Override
    public void assertion(ClientHttpResponse response) {
        try {
            assertEquals(JsonPath.parse(response.getBody()).read(jsonPath, String.class), expectedValue);
        } catch (IOException | AssertionError e) {
            throw new ITAssertionError("For path " + jsonPath + " " + e.getMessage());
        }
    }
}
