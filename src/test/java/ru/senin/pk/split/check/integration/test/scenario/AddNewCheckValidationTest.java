package ru.senin.pk.split.check.integration.test.scenario;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.senin.pk.split.check.integration.test.steps.*;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITResponseBodyAssertion;
import ru.senin.pk.split.check.integration.test.utils.ITResponseStatusAssertion;

public class AddNewCheckValidationTest {

    @Test(description = "Add new check validations test")
    @Parameters("ORIGIN")
    public void test(String origin) {

        // Initialize users

        SignUpAndSignInStep signUpAndSignIn = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user1Cookies = signUpAndSignIn.getOutParams().get(ITParam.COOKIES);

        // Initialize checks

        GetCurrentUserChecksStep getCurrentUserChecksStep = new GetCurrentUserChecksStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddNewCheckStep addNewCheckStep_withoutName = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck_without_name.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "name"))
                .execute();

        AddNewCheckStep addNewCheckStep_emptyName = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck_empty_name.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "name"))
                .execute();

        AddNewCheckStep addNewCheckStep_withoutDate = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck_without_date.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "date"))
                .execute();

        AddNewCheckStep addNewCheckStep_emptyDate = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck_empty_date.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "date"))
                .execute();

        AddNewCheckStep addNewCheckStep_invalidDateFormat = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck_empty_date.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "date"))
                .execute();

        AddNewCheckStep addNewCheck_success = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();
    }
}
