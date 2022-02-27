package ru.senin.pk.split.check.integration.test.utils;

import org.springframework.http.client.ClientHttpResponse;

/**
 * Interface for integration tests http response assertions
 */
public interface ITResponseAssertion {

    /**
     * Assertion method
     *
     * @param response
     * @throws ITAssertionError if assert failed
     */
    void assertion(ClientHttpResponse response);
}
