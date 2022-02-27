package ru.senin.pk.split.check.integration.test.scenario;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.senin.pk.split.check.integration.test.steps.*;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITResponseBodyAssertion;
import ru.senin.pk.split.check.integration.test.utils.ITResponseStatusAssertion;

import java.util.Arrays;

public class EndToEndTest {

    @Test(description = "End-to-end test")
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

        SignUpAndSignInStep signUpAndSignIn_user3 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user3Username = signUpAndSignIn_user3.getOutParams().get(ITParam.USERNAME);
        String user3Cookies = signUpAndSignIn_user3.getOutParams().get(ITParam.COOKIES);
        String user3Id = signUpAndSignIn_user3.getOutParams().get(ITParam.USER_ID);

        SignUpAndSignInStep signUpAndSignIn_user4 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user4Username = signUpAndSignIn_user4.getOutParams().get(ITParam.USERNAME);
        String user4Cookies = signUpAndSignIn_user4.getOutParams().get(ITParam.COOKIES);
        String user4Id = signUpAndSignIn_user4.getOutParams().get(ITParam.USER_ID);

        GetFriendsStep getFriendsStep_user1_1 = new GetFriendsStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddFriendStep addFriendStep_user1_1 = new AddFriendStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user2Id)
                .withTemplate("AddFriend.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        GetFriendsStep getFriendsStep_user1_2 = new GetFriendsStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        GetFriendsStep getFriendsStep_user2_1 = new GetFriendsStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user2Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AcceptFriendRequestStep acceptFriendRequestStep_user2_1 = new AcceptFriendRequestStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user2Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user1Id)
                .withTemplate("AcceptFriendRequest.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddFriendStep addFriendStep_user1_2 = new AddFriendStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user3Id)
                .withTemplate("AddFriend.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AcceptFriendRequestStep acceptFriendRequestStep_user3_1 = new AcceptFriendRequestStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user3Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user1Id)
                .withTemplate("AcceptFriendRequest.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddFriendStep addFriendStep_user1_3 = new AddFriendStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user4Id)
                .withTemplate("AddFriend.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AcceptFriendRequestStep acceptFriendRequestStep_user4_1 = new AcceptFriendRequestStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user4Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user1Id)
                .withTemplate("AcceptFriendRequest.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        GetFriendsStep getFriendsStep_user2_2 = new GetFriendsStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user2Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        // Initialize checks

        GetCurrentUserChecksStep getCurrentUserChecksStep_user1_1 = new GetCurrentUserChecksStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddNewCheckStep addNewCheckStep_user1_1 = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        String checkId = addNewCheckStep_user1_1.getOutParams().get(ITParam.CHECK_ID);

        GetCurrentUserChecksStep getCurrentUserChecksStep_user1_2 = new GetCurrentUserChecksStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddCheckUserStep addCheckUserStep_user1_1 = new AddCheckUserStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.USER_ID, user2Id)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddCheckUserStep addCheckUserStep_user1_2 = new AddCheckUserStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.USER_ID, user3Id)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddCheckUserStep addCheckUserStep_user1_3 = new AddCheckUserStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.USER_ID, user4Id)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        // Initialize purchases

        GetPurchasesStep getPurchasesStep_user1_1 = new GetPurchasesStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_user1_1 = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id, user3Id))
                .withInParam(ITParam.PURCHASE_COST, "4040")
                .withTemplate("AddNewPurchase.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_user1_2 = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user2Id, user3Id))
                .withInParam(ITParam.PURCHASE_COST, "3541")
                .withTemplate("AddNewPurchase.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        AddNewPurchaseStep addNewPurchaseStep_user1_3 = new AddNewPurchaseStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.PAYER_ID, user1Id)
                .withInParam(ITParam.CONSUMERS_IDS, Arrays.asList(user1Id, user3Id, user4Id))
                .withInParam(ITParam.PURCHASE_COST, "1980")
                .withTemplate("AddNewPurchase.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        GetPurchasesStep getPurchasesStep_user1_2 = new GetPurchasesStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        // Result transactions

        GetCurrentUserChecksStep getCurrentUserChecksStep_user1_3 = new GetCurrentUserChecksStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[0].amount", "3187.0"))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[0].payerUser", user1Id))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[0].consumerUser", user3Id))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[1].amount", "2527.0"))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[1].payerUser", user1Id))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[1].consumerUser", user2Id))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[2].amount", "660.0"))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[2].payerUser", user1Id))
                .withAssertion(ITResponseBodyAssertion.assertBody("$[0].transfers[2].consumerUser", user4Id))
                .execute();
    }
}
