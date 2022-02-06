package ru.senin.pk.split.check.services;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import ru.senin.pk.split.check.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.*;

public class CheckTransfersServiceTest {

    @InjectMocks
    private CheckTransfersService checkTransfersService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DataProvider(name = "testCalculateTransfers_DP")
    public Object[][] testCalculateTransfers_DP() {
        return new Object[][]{
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("100.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("200.00"), user(2L), Lists.newArrayList(user(1L), user(2L))),
                                purchase(new BigDecimal("300.00"), user(3L), Lists.newArrayList(user(2L), user(3L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(3L), user(2L), new BigDecimal("83.33")),
                                transfer(user(3L), user(1L), new BigDecimal("33.34"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L), user(4L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("100.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L))),
                                purchase(new BigDecimal("200.00"), user(2L), Lists.newArrayList(user(1L), user(2L))),
                                purchase(new BigDecimal("300.00"), user(2L), Lists.newArrayList(user(2L), user(4L))),
                                purchase(new BigDecimal("500.00"), user(3L), Lists.newArrayList(user(2L), user(3L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(3L), user(4L), new BigDecimal("175.00")),
                                transfer(user(3L), user(1L), new BigDecimal("25.00")),
                                transfer(user(3L), user(2L), new BigDecimal("25.00"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("3600.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("308.00"), user(3L), Lists.newArrayList(user(2L))),
                                purchase(new BigDecimal("72.00"), user(1L), Lists.newArrayList(user(2L))),
                                purchase(new BigDecimal("244.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1783.00"), user(1L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("1248.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1070.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1650.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("3000.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1550.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("3300.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1290.00"), user(3L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("844.00"), user(3L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1290.00"), user(2L), Lists.newArrayList(user(2L), user(3L))),
                                purchase(new BigDecimal("3000.00"), user(2L), Lists.newArrayList(user(1L), user(2L))),
                                purchase(new BigDecimal("1158.00"), user(2L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("2475.00"), user(2L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1312.00"), user(2L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1338.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("2767.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("160.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1550.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(1L), user(3L), new BigDecimal("7721.67")),
                                transfer(user(1L), user(2L), new BigDecimal("2808.67"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L), user(4L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("980.00"), user(1L), Lists.newArrayList(user(1L), user(2L))),
                                purchase(new BigDecimal("1187.00"), user(1L), Lists.newArrayList(user(1L), user(2L))),
                                purchase(new BigDecimal("317.00"), user(1L), Lists.newArrayList(user(1L), user(2L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(1L), user(2L), new BigDecimal("1242.00"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L), user(4L), user(5L), user(6L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("2100.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L), user(5L), user(6L))),
                                purchase(new BigDecimal("2400.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L), user(5L), user(6L))),
                                purchase(new BigDecimal("350.00"), user(4L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("350.00"), user(6L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("350.00"), user(5L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("920.00"), user(3L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("760.00"), user(4L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("760.00"), user(5L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("750.00"), user(2L), Lists.newArrayList(user(1L))),
                                purchase(new BigDecimal("2160.00"), user(1L), Lists.newArrayList(user(3L), user(4L), user(6L))),
                                purchase(new BigDecimal("700.00"), user(6L), Lists.newArrayList(user(1L)))

                        ),
                        Lists.newArrayList(
                                transfer(user(1L), user(3L), new BigDecimal("550.00")),
                                transfer(user(1L), user(6L), new BigDecimal("420.00")),
                                transfer(user(5L), user(4L), new BigDecimal("360.00"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("1500.00"), user(3L), Lists.newArrayList(user(2L), user(3L))),
                                purchase(new BigDecimal("3130.00"), user(3L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("320.00"), user(3L), Lists.newArrayList(user(2L), user(3L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(3L), user(2L), new BigDecimal("1953.33")),
                                transfer(user(3L), user(1L), new BigDecimal("1043.33"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("850.00"), user(1L), Lists.newArrayList(user(1L), user(3L))),
                                purchase(new BigDecimal("2150.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(1L), user(3L), new BigDecimal("1141.67")),
                                transfer(user(1L), user(2L), new BigDecimal("716.67"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L), user(4L), user(5L), user(6L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("850.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(5L), user(6L))),
                                purchase(new BigDecimal("300.00"), user(1L), Lists.newArrayList(user(4L))),
                                purchase(new BigDecimal("220.00"), user(1L), Lists.newArrayList(user(6L))),
                                purchase(new BigDecimal("160.00"), user(1L), Lists.newArrayList(user(5L))),
                                purchase(new BigDecimal("200.00"), user(1L), Lists.newArrayList(user(1L), user(3L))),
                                purchase(new BigDecimal("280.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(5L))),
                                purchase(new BigDecimal("230.00"), user(1L), Lists.newArrayList(user(5L))),
                                purchase(new BigDecimal("80.00"), user(1L), Lists.newArrayList(user(4L))),
                                purchase(new BigDecimal("70.00"), user(1L), Lists.newArrayList(user(2L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(1L), user(5L), new BigDecimal("630.00")),
                                transfer(user(1L), user(6L), new BigDecimal("390.00")),
                                transfer(user(1L), user(4L), new BigDecimal("380.00")),
                                transfer(user(1L), user(3L), new BigDecimal("340.00")),
                                transfer(user(1L), user(2L), new BigDecimal("310.00"))
                        )
                },
                {
                        Lists.newArrayList(user(1L), user(2L), user(3L), user(4L)),
                        Lists.newArrayList(
                                purchase(new BigDecimal("176.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L))),
                                purchase(new BigDecimal("3724.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L))),
                                purchase(new BigDecimal("2121.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L))),
                                purchase(new BigDecimal("1720.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L))),
                                purchase(new BigDecimal("1300.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L))),
                                purchase(new BigDecimal("1330.00"), user(1L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("200.00"), user(1L), Lists.newArrayList(user(2L))),
                                purchase(new BigDecimal("1800.00"), user(2L), Lists.newArrayList(user(1L), user(2L), user(3L))),
                                purchase(new BigDecimal("1324.00"), user(4L), Lists.newArrayList(user(1L), user(2L), user(3L), user(4L)))
                        ),
                        Lists.newArrayList(
                                transfer(user(1L), user(3L), new BigDecimal("3634.58")),
                                transfer(user(1L), user(2L), new BigDecimal("2034.58")),
                                transfer(user(1L), user(4L), new BigDecimal("1267.25"))
                        )
                },
        };
    }

    @Test(description = "Calculates check transactions", dataProvider = "testCalculateTransfers_DP")
    public void testCalculateTransfers(List<User> sourceUsers, List<Purchase> sourcePurchases, List<Transfer> expectedTransfers) {
        Check check = new Check();
        check.setUsers(sourceUsers);
        check.setPurchases(sourcePurchases);

        checkTransfersService.calculateTransfers(check);

        List<Transfer> resultTransfers = check.getTransfers();
        assertEquals(resultTransfers.size(), expectedTransfers.size());
        for (int i = 0; i < resultTransfers.size(); i++) {
            assertTransferEquals(resultTransfers.get(i), expectedTransfers.get(i));
        }
    }

    private User user(Long id) {
        return new RegisteredUser(id, "sample_user_" + id);
    }

    private static final Random RANDOM = new Random();

    private Purchase purchase(BigDecimal cost, User payer, List<User> consumers) {
        Long id = RANDOM.nextLong();
        return new Purchase(id, "sample_purchase_" + id, cost, payer, consumers);
    }

    private Transfer transfer(User payer, User consumer, BigDecimal amount) {
        return new Transfer(payer, consumer, amount);
    }

    private void assertTransferEquals(Transfer o1, Transfer o2) {
        assertEquals(o1.getPayer().getId(), o2.getPayer().getId());
        assertEquals(o1.getConsumer().getId(), o2.getConsumer().getId());
        assertEquals(o1.getAmount(), o2.getAmount());
    }
}