package com.invillia.banktestunitintegration.integrationTest;

import com.invillia.banktestunitintegration.BankTestUnitIntegrationApplication;
import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.request.WithdrawRequest;
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
public class WithdrawAccountIntegrationTest {

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
    void shouldWithdrawInAccountWithIdCustomerNotFoundTest() {

        accountFactory.create();

        WithdrawRequest withdrawRequest = new WithdrawRequest(500.00, 2L);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(withdrawRequest)
                .when()
                .post("/accounts/withdraw")
                .then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("message", Matchers.is("Conta de ID 2 não encontrada!"));

    }


    @Test
    void shouldithdrawInAccountWithSucessTest() {

        final Account account = accountFactory.create();

        WithdrawRequest withdrawRequest = new WithdrawRequest(500.00, 1L);

        final Double balance = account.getBalance() - withdrawRequest.getWithdraw();

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(withdrawRequest)
                .when()
                .post("/accounts/withdraw")
                .then()
                .log().all()
                .statusCode(SC_OK);

        final Account accountWithWithdraw = accountRepository.findById(1L).get();

        Assertions.assertAll("account assert",
                () -> Assertions.assertEquals(balance, accountWithWithdraw.getBalance()));
    }


    @Test
    void shouldWithdrawInAccountWithoutLimitTest() {

        final Account account = accountFactory.create();

        WithdrawRequest withdrawRequest = new WithdrawRequest(2000.00, 1L);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(withdrawRequest)
                .when()
                .post("/accounts/withdraw")
                .then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("message", Matchers.is("Limite de R$ 200.0 excedido!"));

        final Account accountWithWithdraw = accountRepository.findById(1L).get();

        Assertions.assertAll("account assert",
                () -> Assertions.assertEquals(account.getBalance(), accountWithWithdraw.getBalance()));
    }


    @Test
    void shouldWithdrawInAccountWithNegativeNumber() {

        final Account account = accountFactory.create();

        WithdrawRequest withdrawRequest = new WithdrawRequest(-200.00, 1L);

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(withdrawRequest)
                .when()
                .post("/accounts/withdraw")
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("errors.defaultMessage[0]", Matchers.is("O valor deve ser maior que zero!"));

        final Account accountWithWithdraw = accountRepository.findById(1L).get();

        Assertions.assertAll("account assert",
                () -> Assertions.assertEquals(account.getBalance(), accountWithWithdraw.getBalance()));
    }
}
