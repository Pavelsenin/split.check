package ru.senin.pk.split.check.integration.test.steps;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.Validate;
import org.springframework.http.*;
import ru.senin.pk.split.check.integration.test.utils.ITAbstractStep;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AddNewCheckStep extends ITAbstractStep<AddNewCheckStep> {

    @Override
    protected void validateInParams(ITParams inParams) {
        Validate.notBlank(inParams.get(ITParam.ORIGIN));
        Validate.notBlank(inParams.get(ITParam.COOKIES));
    }

    private static final String PATH = "/checks/new";

    @Override
    protected ITParams executionImpl(ITParams inParams) {
        String origin = inParams.get(ITParam.ORIGIN);
        String url = origin + PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, inParams.get(ITParam.COOKIES));

        int random = (int) (Math.random() * 1000000);
        String randomCheckName = "sample_check_" + random;
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

        ITParams templateParams = new ITParams(inParams);
        templateParams.put(ITParam.GENERATED_CHECK_NAME, randomCheckName);
        templateParams.put(ITParam.GENERATED_CHECK_DATE, currentDate);
        String requestBody = resolveTemplate(templateParams);

        HttpEntity<String> request = new HttpEntity(requestBody, headers);

        ResponseEntity<String> response = getRestTemplate()
                .exchange(url, HttpMethod.POST, request, String.class);

        if (!Objects.equals(response.getStatusCode(), HttpStatus.OK)) {
            return new ITParams();
        } else {
            ITParams outParams = new ITParams();
            outParams.put(ITParam.GENERATED_CHECK_NAME, randomCheckName);
            outParams.put(ITParam.GENERATED_CHECK_DATE, currentDate);
            String checkId = JsonPath.parse(response.getBody()).read("$.id", String.class);
            outParams.put(ITParam.CHECK_ID, checkId);
            return outParams;
        }
    }
}
