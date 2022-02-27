package ru.senin.pk.split.check.integration.test.utils;

import java.util.*;

/**
 * Integration test step
 */
public interface ITStep<T extends ITStep> {

    /**
     * Add input param
     *
     * @param param
     * @param value
     * @return
     */
    T withInParam(ITParam param, Object value);

    /**
     * Add input params
     *
     * @param params
     * @return
     */
    T withInParams(ITParams params);

    /**
     * Add assertion
     *
     * @param assertions
     * @return
     */
    T withAssertion(ITResponseAssertion assertion);

    /**
     * Set request body template name
     *
     * @param templateName
     * @return
     */
    T withTemplate(String templateName);

    /**
     * Receive step execution status
     *
     * @return
     */
    ITStepStatus getStatus();

    /**
     * Receive step output params
     *
     * @return
     */
    ITParams getOutParams();

    /**
     * Execute integration test step
     */
    T execute();
}
