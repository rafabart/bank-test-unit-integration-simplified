package com.invillia.banktestunitintegration.integrationTest;

import com.invillia.banktestunitintegration.BankTestUnitIntegrationApplication;
import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.enums.AccountTypeEnum;
import com.invillia.banktestunitintegration.factory.AccountFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = BankTestUnitIntegrationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FindAccountIntegrationTest {

    @LocalServerPort
    private int portApi;

    @Autowired
    private AccountFactory accountFactory;

    @BeforeEach
    public void init() {
        RestAssured.port = portApi;
    }

    @Test
    void shouldFindAllWithSuccessTest() {

        accountFactory.create(10);

        RestAssured
                .given()
                .log().all()
                .when()
                .get("/accounts")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("$", Matchers.hasSize(10));
    }


    @Test
    void shouldFindAllByNumberAccountWithSuccessTest() {

        accountFactory.create(10);

        Account account = new Account();
        account.setNumberAccount("999999");

        accountFactory.create(account);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("numberAccount", account.getNumberAccount())
                .when()
                .get("/accounts")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("$", Matchers.hasSize(1))
                .body("numberAccount[0]", Matchers.is(account.getNumberAccount()));
    }


    @Test
    void shouldFindAllByAgencyWithSuccessTest() {

        accountFactory.create(10);

        Account account = new Account();
        account.setAgency("9999");

        accountFactory.create(account);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("agency", account.getAgency())
                .when()
                .get("/accounts")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("$", Matchers.hasSize(1))
                .body("agency[0]", Matchers.is(account.getAgency()));
    }


    @Test
    void shouldFindAllByBalanceWithSuccessTest() {

        accountFactory.create(10);

        Account account = new Account();
        account.setBalance(999999.99);

        accountFactory.create(account);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("balance", account.getBalance())
                .when()
                .get("/accounts")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("$", Matchers.hasSize(1))
                .body("balance[0]", Matchers.is(account.getBalance().floatValue()));
    }

    @Test
    void shouldFindAllByLimitAccountWithSuccessTest() {

        accountFactory.create(10);

        Account account = new Account();
        account.setLimitAccount(9999.99);

        accountFactory.create(account);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("limitAccount", account.getLimitAccount())
                .when()
                .get("/accounts")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("$", Matchers.hasSize(1))
                .body("limitAccount[0]", Matchers.is(account.getLimitAccount().floatValue()));
    }

    @Test
    void shouldFindAllByAccountTypeEnumtWithSuccessTest() {

        accountFactory.create(10);

        Account account = new Account();
        account.setAccountTypeEnum(AccountTypeEnum.CHECKING);

        accountFactory.create(account);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("accountTypeString", account.getAccountTypeEnum().toString())
                .when()
                .get("/accounts")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("$", Matchers.hasSize(1))
                .body("accountTypeString[0]", Matchers.is(account.getAccountTypeEnum().toString()));
    }

    @Test
    void shouldFindAllByCustomerIdWithSuccessTest() {

        accountFactory.create(10);

        final int idCustomer = 1;

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("idCustomer", idCustomer)
                .when()
                .get("/accounts")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("$", Matchers.hasSize(1))
                .body("customer[0].id", Matchers.is(idCustomer));
    }
}
