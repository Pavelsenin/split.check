package ru.senin.pk.split.check.integration.test.utils;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.testng.Assert.*;

/**
 * Integration tests http response status assertion
 */
public class ITResponseStatusAssertion implements ITResponseAssertion {

    private HttpStatus expectedStatus;

    private ITResponseStatusAssertion(HttpStatus expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    public static ITResponseStatusAssertion assertStatus(HttpStatus expectedStatus) {
        Validate.notNull(expectedStatus);
        return new ITResponseStatusAssertion(expectedStatus);
    }

    @Override
    public void assertion(ClientHttpResponse response) {
        try {
            assertEquals(response.getStatusCode(), expectedStatus);
        } catch (IOException | AssertionError e) {
            throw new ITAssertionError(e.getMessage());
        }
    }
}
