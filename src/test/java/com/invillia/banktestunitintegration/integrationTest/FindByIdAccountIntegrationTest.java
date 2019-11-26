package com.invillia.banktestunitintegration.integrationTest;

import com.invillia.banktestunitintegration.BankTestUnitIntegrationApplication;
import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.factory.AccountFactory;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.format.DateTimeFormatter;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = BankTestUnitIntegrationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FindByIdAccountIntegrationTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @LocalServerPort
    private int portApi;

    @Autowired
    private AccountFactory accountFactory;

    @BeforeEach
    public void init() {
        RestAssured.port = portApi;
    }


    @Test
    public void shouldFindByIdAccountWithSuccessTest() {

        final Account account = accountFactory.create();

        RestAssured
                .given()
                .log().all()
                .when()
                .get("/accounts/1")
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("id", Matchers.is(1))
                .body("numberAccount", Matchers.is(account.getNumberAccount()))
                .body("agency", Matchers.is(account.getAgency()))
                .body("balance", Matchers.is(account.getBalance().floatValue()))
                .body("limitAccount", Matchers.is(account.getLimitAccount().floatValue()))
                .body("accountTypeString", Matchers.is(account.getAccountTypeEnum().toString()))
                .body("customer.id", Matchers.is(account.getCustomer().getId().intValue()))
                .body("createdAt", Matchers.is(account.getCreatedAt().format(FORMATTER)))
                .body("updatedAt", Matchers.is(account.getUpdatedAt().format(FORMATTER)));
    }

    @Test
    public void shouldFindByIdNotFoundTest() {

        RestAssured
                .given()
                .log().all()
                .when()
                .get("/accounts/1")
                .then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}