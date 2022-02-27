package ru.senin.pk.split.check.integration.test.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.*;

import java.util.*;

/**
 * Abstract integration test step
 */
public abstract class ITAbstractStep<T extends ITAbstractStep> implements ITStep<T> {

    /**
     * Step input params, must be set before execute call
     */
    private ITParams inParams;

    /**
     * Step assertions list, must be set before execute call
     */
    private List<ITResponseAssertion> assertions;

    /**
     * Request body template name
     */
    private String templateName;

    /**
     * Step output params, initializes after execute call
     */
    private ITParams outParams;

    /**
     * Current step status, indicates step was executed and execution result
     */
    private ITStepStatus status;

    private ITTemplateService templateService;

    private TestRestTemplate restTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * Step input params validation
     *
     * @param inParams
     * @throws AssertionError if validation failed
     */
    protected abstract void validateInParams(ITParams inParams);

    /**
     * Step execution implementation using this#restTemplate
     *
     * @param inParams
     * @return
     */
    protected abstract ITParams executionImpl(ITParams inParams);

    public ITAbstractStep() {
        inParams = new ITParams();
        assertions = new ArrayList<>();
        outParams = new ITParams();
        status = ITStepStatus.NOT_EXECUTED;
        templateService = new ITTemplateService();
        // As the interceptor consumes the response stream, we use BufferingClientHttpRequestFactory in tests
        // Using HttpComponentsClientHttpRequestFactory instead of org.springframework.http.client.SimpleClientHttpRequestFactory
        // cause it allows to process response body with statuses differs from 200
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory());
        List<ClientHttpRequestInterceptor> interceptors = Arrays.asList(
                new ITAssertionInterceptor(this),
                new ITLoggingInterceptor(this)
        );
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .requestFactory(() -> factory)
                .interceptors(interceptors);
        restTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Override
    public T withInParam(ITParam param, Object value) {
        this.inParams.put(param, value);
        return (T) this;
    }

    @Override
    public T withInParams(ITParams params) {
        this.inParams.putAll(params);
        return (T) this;
    }

    @Override
    public T withAssertion(ITResponseAssertion assertion) {
        this.assertions.add(assertion);
        return (T) this;
    }

    public List<ITResponseAssertion> getAssertions() {
        return this.assertions;
    }

    @Override
    public T withTemplate(String templateName) {
        this.templateName = templateName;
        return (T) this;
    }

    @Override
    public ITParams getOutParams() {
        return new ITParams(outParams);
    }

    @Override
    public ITStepStatus getStatus() {
        return status;
    }

    @Override
    public T execute() {
        LOGGER.info("Execute start. inParams: {}", inParams);
        try {
            validateInParams(inParams);
        } catch (AssertionError e) {
            this.status = ITStepStatus.ERROR;
            LOGGER.info("Invalid inParams: {}", e.getMessage());
            throw e;
        }
        ITParams outParams;
        try {
            outParams = executionImpl(inParams);
        } catch (ITAssertionError e) {
            this.status = ITStepStatus.FAILED;
            LOGGER.info("Execute assertion failed, errors:\n{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            this.status = ITStepStatus.ERROR;
            LOGGER.info("Execute error: ", e);
            throw e;
        }
        this.outParams = outParams;
        this.status = ITStepStatus.SUCCESS;
        LOGGER.info("Execute success. outParams: {}", outParams);
        return (T) this;
    }

    protected String resolveTemplate(Map<String, Object> input) {
        if (StringUtils.isBlank(this.templateName)) {
            return StringUtils.EMPTY;
        }
        return templateService.resolve(this.templateName, input);
    }

    protected TestRestTemplate getRestTemplate() {
        return this.restTemplate;
    }
}
