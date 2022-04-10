package ru.senin.pk.split.check.integration.test.scenario;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.senin.pk.split.check.integration.test.steps.*;
import ru.senin.pk.split.check.integration.test.utils.ITParam;
import ru.senin.pk.split.check.integration.test.utils.ITResponseBodyAssertion;
import ru.senin.pk.split.check.integration.test.utils.ITResponseStatusAssertion;

import java.util.Arrays;
import java.util.List;

public class UpdateCheckUsersTest {

    @Test(description = "Update check users test")
    @Parameters("ORIGIN")
    public void test(String origin) {

        // Initialize users

        SignUpAndSignInStep signUpAndSignIn_user1 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user1Username = signUpAndSignIn_user1.getOutParams().get(ITParam.USERNAME);
        String user1Cookies = signUpAndSignIn_user1.getOutParams().get(ITParam.COOKIES);
        String user1Id = signUpAndSignIn_user1.getOutParams().get(ITParam.USER_ID);


        // Accepted friend
        SignUpAndSignInStep signUpAndSignIn_user2 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user2Username = signUpAndSignIn_user2.getOutParams().get(ITParam.USERNAME);
        String user2Cookies = signUpAndSignIn_user2.getOutParams().get(ITParam.COOKIES);
        String user2Id = signUpAndSignIn_user2.getOutParams().get(ITParam.USER_ID);

        // Non accepted friend
        SignUpAndSignInStep signUpAndSignIn_user3 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user3Username = signUpAndSignIn_user3.getOutParams().get(ITParam.USERNAME);
        String user3Cookies = signUpAndSignIn_user3.getOutParams().get(ITParam.COOKIES);
        String user3Id = signUpAndSignIn_user3.getOutParams().get(ITParam.USER_ID);

        // Non friend
        SignUpAndSignInStep signUpAndSignIn_user4 = new SignUpAndSignInStep()
                .withInParam(ITParam.ORIGIN, origin)
                .execute();
        String user4Username = signUpAndSignIn_user4.getOutParams().get(ITParam.USERNAME);
        String user4Cookies = signUpAndSignIn_user4.getOutParams().get(ITParam.COOKIES);
        String user4Id = signUpAndSignIn_user4.getOutParams().get(ITParam.USER_ID);

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

        AddFriendStep addFriendStep_user1_2 = new AddFriendStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.FRIEND_USER_ID, user3Id)
                .withTemplate("AddFriend.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        // Initialize check

        AddNewCheckStep addNewCheckStep = new AddNewCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withTemplate("AddNewCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();
        String checkId = addNewCheckStep.getOutParams().get(ITParam.CHECK_ID);
        
        // Add accepted friend

        String changedName = "changed_name";
        String changedDate = "01.01.1970";
        List<String> changedUserIds = Arrays.asList(new String[]{user1Id, user2Id});

        UpdateCheckStep updateCheckStep_addAcceptedFriend = new UpdateCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.CHECK_NAME, changedName)
                .withInParam(ITParam.CHECK_DATE, changedDate)
                .withInParam(ITParam.CHECK_USERS_IDS, changedUserIds)
                .withTemplate("UpdateCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        // Add non accepted friend
        changedUserIds = Arrays.asList(new String[]{user1Id, user2Id, user3Id});

        UpdateCheckStep updateCheckStep_addNonAcceptedFriend = new UpdateCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.CHECK_NAME, changedName)
                .withInParam(ITParam.CHECK_DATE, changedDate)
                .withInParam(ITParam.CHECK_USERS_IDS, changedUserIds)
                .withTemplate("UpdateCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "users"))
                .execute();

        // Add non friend
        changedUserIds = Arrays.asList(new String[]{user1Id, user2Id, user4Id});

        UpdateCheckStep updateCheckStep_addNonFriend = new UpdateCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.CHECK_NAME, changedName)
                .withInParam(ITParam.CHECK_DATE, changedDate)
                .withInParam(ITParam.CHECK_USERS_IDS, changedUserIds)
                .withTemplate("UpdateCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.BAD_REQUEST))
                .withAssertion(ITResponseBodyAssertion.assertBody("$.fieldErrors[0].field", "users"))
                .execute();

        // Remove user
        changedUserIds = Arrays.asList(new String[]{user1Id});

        UpdateCheckStep updateCheckStep_removeUser = new UpdateCheckStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withInParam(ITParam.CHECK_ID, checkId)
                .withInParam(ITParam.CHECK_NAME, changedName)
                .withInParam(ITParam.CHECK_DATE, changedDate)
                .withInParam(ITParam.CHECK_USERS_IDS, changedUserIds)
                .withTemplate("UpdateCheck.ftl")
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();

        GetCurrentUserChecksStep getCurrentUserChecksStep = new GetCurrentUserChecksStep()
                .withInParam(ITParam.ORIGIN, origin)
                .withInParam(ITParam.COOKIES, user1Cookies)
                .withAssertion(ITResponseStatusAssertion.assertStatus(HttpStatus.OK))
                .execute();
    }
}
