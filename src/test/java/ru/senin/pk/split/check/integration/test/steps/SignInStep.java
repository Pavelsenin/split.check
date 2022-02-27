package ru.senin.pk.split.check.integration.test.steps;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.Validate;
import org.springframework.http.*;
import ru.senin.pk.split.check.integration.test.utils.ITAbstractStep;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITParams;

public class SignInStep extends ITAbstractStep<SignInStep> {

    @Override
    protected void validateInParams(ITParams inParams) {
        Validate.notBlank(inParams.get(ITParam.ORIGIN));
        Validate.notBlank(inParams.get(ITParam.USERNAME));
        Validate.notBlank(inParams.get(ITParam.PASSWORD));
    }

    private static final String PATH = "/users/current";

    @Override
    protected ITParams executionImpl(ITParams inParams) {
        String origin = inParams.get(ITParam.ORIGIN);
        String url = origin + PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String username = inParams.get(ITParam.USERNAME);
        String password = inParams.get(ITParam.PASSWORD);
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = getRestTemplate()
                .withBasicAuth(username, password)
                .exchange(url, HttpMethod.GET, request, String.class);

        String cookies = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        ITParams responseParams = new ITParams();
        responseParams.put(ITParam.COOKIES, cookies);
        String currentUserId = JsonPath.parse(response.getBody()).read("$.id", String.class);
        responseParams.put(ITParam.USER_ID, currentUserId);

        return responseParams;
    }
}
