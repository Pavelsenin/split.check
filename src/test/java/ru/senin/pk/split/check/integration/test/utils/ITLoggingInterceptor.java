package ru.senin.pk.split.check.integration.test.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * {@link org.springframework.web.client.RestTemplate} interceptor, witch logs request and response messages
 */
public class ITLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final ITAbstractStep step;

    private final Logger LOGGER;

    public ITLoggingInterceptor(ITAbstractStep step) {
        this.step = step;
        this.LOGGER = LoggerFactory.getLogger(step.getClass());
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        LOGGER.info("Raw request. method: {}, URI: {},\nheaders: {},\nbody: {}", request.getMethod(), request.getURI(), request.getHeaders(), new String(body, StandardCharsets.UTF_8));
        ClientHttpResponse response = execution.execute(request, body);
        if (LOGGER.isInfoEnabled()) {
            InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
            String responseBody = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
            LOGGER.info("Raw response. status code: {}, status message: {},\nheaders: {},\nbody: {}", response.getStatusCode(), response.getStatusText(), response.getHeaders(), responseBody);
        }
        return response;
    }
}
