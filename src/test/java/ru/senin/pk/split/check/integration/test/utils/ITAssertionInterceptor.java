package ru.senin.pk.split.check.integration.test.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * {@link org.springframework.web.client.RestTemplate} interceptor, witch asserts response message
 */
public class ITAssertionInterceptor implements ClientHttpRequestInterceptor {

    private final ITAbstractStep step;

    private final Logger LOGGER;

    public ITAssertionInterceptor(ITAbstractStep step) {
        this.step = step;
        LOGGER = LoggerFactory.getLogger(step.getClass());
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        List<ITResponseAssertion> assertions = step.getAssertions();
        if (CollectionUtils.isEmpty(assertions)) {
            LOGGER.debug("Assertions missed");
            return response;
        }
        List<ITAssertionError> assertionErrors = new ArrayList<>();
        for (ITResponseAssertion assertion : assertions) {
            try {
                assertion.assertion(response);
            } catch (ITAssertionError e) {
                assertionErrors.add(e);
            }
        }
        if (CollectionUtils.isEmpty(assertionErrors)) {
            LOGGER.info("Assertion passed");
        } else {
            String errorMessage = IntStream.range(0, assertionErrors.size())
                    .mapToObj(n -> ((n + 1) + ") " + assertionErrors.get(n).getMessage()))
                    .collect(Collectors.joining(",\n"));
            throw new ITAssertionError(errorMessage);
        }
        return response;
    }
}
