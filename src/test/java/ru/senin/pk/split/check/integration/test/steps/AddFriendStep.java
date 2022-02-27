package ru.senin.pk.split.check.integration.test.steps;

import org.apache.commons.lang3.Validate;
import org.springframework.http.*;
import ru.senin.pk.split.check.integration.test.utils.ITAbstractStep;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITParams;

public class AddFriendStep extends ITAbstractStep<AddFriendStep> {

    @Override
    protected void validateInParams(ITParams inParams) {
        Validate.notBlank(inParams.get(ITParam.ORIGIN));
        Validate.notBlank(inParams.get(ITParam.COOKIES));
        Validate.notBlank(inParams.get(ITParam.FRIEND_USER_ID));
    }


    private static final String PATH = "/friends/add";

    @Override
    protected ITParams executionImpl(ITParams inParams) {
        String origin = inParams.get(ITParam.ORIGIN);
        String url = origin + PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String cookies = inParams.get(ITParam.COOKIES);
        headers.set(HttpHeaders.COOKIE, cookies);

        ITParams templateParams = new ITParams(inParams);
        String requestBody = resolveTemplate(templateParams);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = getRestTemplate()
                .exchange(url, HttpMethod.POST, request, String.class);

        return new ITParams();
    }
}
