package ru.senin.pk.split.check.integration.test.steps;

import org.apache.commons.lang3.Validate;
import org.springframework.http.*;
import ru.senin.pk.split.check.integration.test.utils.ITAbstractStep;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITParams;

import java.util.Objects;

public class UpdateCheckStep extends ITAbstractStep<UpdateCheckStep> {

    @Override
    protected void validateInParams(ITParams inParams) {
        Validate.notBlank(inParams.get(ITParam.ORIGIN));
        Validate.notBlank(inParams.get(ITParam.COOKIES));
        Validate.notBlank(inParams.get(ITParam.CHECK_ID));
        Validate.notBlank(inParams.get(ITParam.CHECK_NAME));
        Validate.notBlank(inParams.get(ITParam.CHECK_DATE));
        Validate.notNull(inParams.get(ITParam.CHECK_USERS_IDS));
    }

    private static final String PATH = "/checks/update";

    @Override
    protected ITParams executionImpl(ITParams inParams) {
        String origin = inParams.get(ITParam.ORIGIN);
        String url = origin + PATH + "?check_id=" + inParams.get(ITParam.CHECK_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, inParams.get(ITParam.COOKIES));

        ITParams templateParams = new ITParams(inParams);
        String requestBody = resolveTemplate(templateParams);

        HttpEntity<String> request = new HttpEntity(requestBody, headers);

        ResponseEntity<String> response = getRestTemplate()
                .exchange(url, HttpMethod.POST, request, String.class);

        if (!Objects.equals(response.getStatusCode(), HttpStatus.OK)) {
            return new ITParams();
        } else {
            ITParams outParams = new ITParams();
            return outParams;
        }
    }
}
