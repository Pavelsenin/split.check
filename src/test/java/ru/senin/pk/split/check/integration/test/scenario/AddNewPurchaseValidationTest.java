package ru.senin.pk.split.check.integration.test.scenario;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.senin.pk.split.check.integration.test.steps.*;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITResponseBodyAssertion;
import ru.senin.pk.split.check.integration.test.utils.ITResponseStatusAssertion;

import java.util.Arrays;

public class AddNewPurchaseValidationTest {

    @Test(description = "Add new check validations test")
    @Parameters("ORIGIN")
    public void test(String origin) {

        // Initialize users

        SignUpAndSignInStep signUpAndSignIn_user1 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user1Username = signUpAndSignIn_user1.getOutParams().get(ITParam.USERNAME);
        String user1Cookies = signUpAndSignIn_user1.getOutParams().get(ITParam.COOKIES);
        String user1Id = signUpAndSignIn_user1.getOutParams().get(ITParam.USER_ID);

        SignUpAndSignInStep signUpAndSignIn_user2 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user2Username = signUpAndSignIn_user2.getOutParams().get(ITParam.USERNAME);
        String user2Cookies = signUpAndSignIn_user2.getOutParams().get(ITParam.COOKIES);
        String user2Id = signUpAndSignIn_user2.getOutParams().get(ITParam.USER_ID);

        AddFriendStep addFriendStep_user1_1 = new AddFriendStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user2Id)
                .withTemplate("AddFriend.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AcceptFriendRequestStep acceptFriendRequestStep_user2_1 = new AcceptFriendRequestStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user2Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user1Id)
                .withTemplate("AcceptFriendRequest.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        // Initialize checks

        AddNewCheckStep addNewCheck = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();
        String checkId = addNewCheck.getOutParams().get(ITParam.CHECK_ID);

        AddCheckUserStep addCheckUserStep_user1_1 = new AddCheckUserStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.USER_ID, user2Id)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        // Initialize purchases

        AddNewPurchaseStep addNewPurchaseStep_withoutName = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_without_name.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "name"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_emptyName = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_empty_name.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "name"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_withoutCost = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_without_cost.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "cost"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_emptyCost = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_empty_cost.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "cost"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_withoutPayer = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_without_payer.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "payer"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_emptyPayer = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_empty_payer.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "payer"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_withoutConsumers = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_without_consumers.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "consumers"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_emptyConsumers = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase_empty_consumers.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "consumers"))
                .execute();


        AddNewPurchaseStep addNewPurchaseStep_nonUniqueUserIdConsumers = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user1Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "consumers"))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_success= new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id))
                .withInParam(ITParam.PURCHASE_COST, "1000")
                .withTemplate("AddNewPurchase.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();
    }
}
