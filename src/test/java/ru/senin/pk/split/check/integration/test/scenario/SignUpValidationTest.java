package ru.senin.pk.split.check.integration.test.scenario;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.senin.pk.split.check.integration.test.steps.*;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITResponseBodyAssertion;
import ru.senin.pk.split.check.integration.test.utils.ITResponseStatusAssertion;

public class SignUpValidationTest {

    @Test(description = "Sign up validations test")
    @Parameters("ORIGIN")
    public void test(String origin) {

        SignUpStep signUp_withoutUsername = new SignUpStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withTemplate("SignUp_without_username.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "username"))
                .execute();

        SignUpStep signUp_emptyUsername = new SignUpStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withTemplate("SignUp_empty_username.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "username"))
                .execute();

        SignUpStep signUp_withoutPassword = new SignUpStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withTemplate("SignUp_without_password.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "password"))
                .execute();

        SignUpStep signUp_emptyPassword = new SignUpStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withTemplate("SignUp_empty_password.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "password"))
                .execute();

        SignUpStep signUp_success = new SignUpStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withTemplate("SignUp.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        SignUpStep signUp_userAlreadyExists = new SignUpStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.USERNAME, signUp_success.getOutParams().get(ITParam.USERNAME))
                .withInParam(ITParam.PASSWORD, signUp_success.getOutParams().get(ITParam.PASSWORD))
                .withTemplate("SignUp.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.CONFLICT))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "username"))
                .execute();

        SignInStep signIn_success = new SignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.USERNAME, signUp_success.getOutParams().get(ITParam.USERNAME))
                .withInParam(ITParam.PASSWORD, signUp_success.getOutParams().get(ITParam.PASSWORD))
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();
    }
}
