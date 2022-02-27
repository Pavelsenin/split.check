package ru.senin.pk.split.check.integration.test.steps;

import org.apache.commons.lang3.Validate;
import org.springframework.http.*;
import ru.senin.pk.split.check.integration.test.utils.ITAbstractStep;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITParams;

public class GetPurchasesStep extends ITAbstractStep<GetPurchasesStep> {

    @Override
    protected void validateInParams(ITParams inParams) {
        Validate.notBlank(inParams.get(ITParam.ORIGIN));
        Validate.notBlank(inParams.get(ITParam.COOKIES));
        Validate.notBlank(inParams.get(ITParam.CHECK_ID));
    }

    private static final String PATH = "/purchases/get";

    @Override
    protected ITParams executionImpl(ITParams inParams) {
        String origin = inParams.get(ITParam.ORIGIN);
        String url = origin + PATH + "?check_id=" + inParams.get(ITParam.CHECK_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, inParams.get(ITParam.COOKIES));

        HttpEntity<String> request = new HttpEntity(headers);

        ResponseEntity<String> response = getRestTemplate()
                .exchange(url, HttpMethod.GET, request, String.class);

        return new ITParams();
    }
}
