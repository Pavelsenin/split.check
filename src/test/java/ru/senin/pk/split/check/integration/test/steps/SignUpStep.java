package ru.senin.pk.split.check.integration.test.steps;

import org.apache.commons.lang3.Validate;
import org.springframework.http.*;
import ru.senin.pk.split.check.integration.test.utils.ITAbstractStep;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITParams;

import java.util.Objects;

public class SignUpStep extends ITAbstractStep<SignUpStep> {

    @Override
    protected void validateInParams(ITParams inParams) {
        Validate.notBlank(inParams.get(ITParam.ORIGIN));
        if (Objects.nonNull(inParams.get(ITParam.USERNAME)) || Objects.nonNull(inParams.get(ITParam.PASSWORD))) {
            Validate.notBlank(inParams.get(ITParam.USERNAME));
            Validate.notBlank(inParams.get(ITParam.PASSWORD));
        }
    }

    private static final String PATH = "/auth/signup";

    @Override
    protected ITParams executionImpl(ITParams inParams) {
        String origin = inParams.get(ITParam.ORIGIN);
        String url = origin + PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        boolean generationRequired = Objects.isNull(inParams.get(ITParam.USERNAME));
        String username;
        String password;
        if (generationRequired) {
            int random = (int) (Math.random() * 1000000);
            username = "sample_username_" + random;
            password = "sample_password_" + random;
        } else {
            username = inParams.get(ITParam.USERNAME);
            password = inParams.get(ITParam.PASSWORD);
        }

        ITParams templateParams = new ITParams(inParams);
        templateParams.put(ITParam.USERNAME, username);
        templateParams.put(ITParam.PASSWORD, password);
        String requestBody = resolveTemplate(templateParams);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = getRestTemplate()
                .exchange(url, HttpMethod.POST, request, String.class);

        ITParams outParams = new ITParams();
        outParams.put(ITParam.USERNAME, username);
        outParams.put(ITParam.PASSWORD, password);
        return outParams;
    }


}
