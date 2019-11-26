package com.invillia.banktestunitintegration.integrationTest;

import com.invillia.banktestunitintegration.BankTestUnitIntegrationApplication;
import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.request.DepositRequest;
import com.invillia.banktestunitintegration.factory.AccountFactory;
import com.invillia.banktestunitintegration.repository.AccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = BankTestUnitIntegrationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepositAccountIntegrationTest {

    @LocalServerPort
    private int portApi;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void init() {
        RestAssured.port = portApi;
    }

    @Test
    void shouldDepositInAccountWithIdCustomerNotFoundTest() {

        final Account account = accountFactory.create();

        final DepositRequest depositRequest = new DepositRequest(2000.00, 2L);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(depositRequest)
                .when()
                .post("/accounts/deposit")
                .then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("message", Matchers.is("Conta de ID 2 não encontrada!"));
    }


    @Test
    void shouldDepositInAccountWithSucessTest() {

        final Account account = accountFactory.create();

        final DepositRequest depositRequest = new DepositRequest(2000.00, 1L);

        final Double balance = depositRequest.getDeposit() + account.getBalance();

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(depositRequest)
                .when()
                .post("/accounts/deposit")
                .then()
                .log().all()
                .statusCode(SC_OK);

        Account accountWithDeposit = accountRepository.findById(1L).get();

        Assertions.assertAll("account assert",
                () -> Assertions.assertEquals(balance, accountWithDeposit.getBalance()));
    }


    @Test
    void shouldDepositInAccountWithNegativeNumber() {

        final Account account = accountFactory.create();

        final DepositRequest depositRequest = new DepositRequest(-2000.00, 1L);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(depositRequest)
                .when()
                .post("/accounts/deposit")
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("errors.defaultMessage[0]", Matchers.is("O valor deve ser maior que zero!"));

        Account accountWithDeposit = accountRepository.findById(1L).get();

        Assertions.assertAll("account assert",
                () -> Assertions.assertEquals(account.getBalance(), accountWithDeposit.getBalance()));
    }
}
