package ru.senin.pk.split.check.integration.test.steps;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;
import ru.senin.pk.split.check.integration.test.utils.ITAbstractStep;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITParams;
import ru.senin.pk.split.check.integration.test.utils.ITResponseStatusAssertion;


public class SignUpAndSignInStep extends ITAbstractStep<SignUpAndSignInStep> {

    @Override
    protected void validateInParams(ITParams inParams) {
        Validate.notBlank(inParams.get(ITParam.ORIGIN));
    }

    @Override
    protected ITParams executionImpl(ITParams inParams) {
        String origin = inParams.get(ITParam.ORIGIN);

        SignUpStep signUp = new SignUpStep()
                .withInParams(inParams)
                .withTemplate("SignUp.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();
        String username = signUp.getOutParams().get(ITParam.USERNAME);
        String password = signUp.getOutParams().get(ITParam.PASSWORD);

        SignInStep signIn = new SignInStep()
                .withInParams(inParams)
                .withInParam(ITParam.USERNAME, username)
                .withInParam(ITParam.PASSWORD, password)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        ITParams responseParams = new ITParams();
        responseParams.put(ITParam.USERNAME, username);
        responseParams.put(ITParam.PASSWORD, password);
        responseParams.put(ITParam.COOKIES, signIn.getOutParams().get(ITParam.COOKIES));
        responseParams.put(ITParam.USER_ID, signIn.getOutParams().get(ITParam.USER_ID));
        return responseParams;
    }
}
