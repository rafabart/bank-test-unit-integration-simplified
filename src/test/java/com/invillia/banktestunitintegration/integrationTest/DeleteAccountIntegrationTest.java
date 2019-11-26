package com.invillia.banktestunitintegration.integrationTest;

import com.invillia.banktestunitintegration.BankTestUnitIntegrationApplication;
import com.invillia.banktestunitintegration.factory.AccountFactory;
import com.invillia.banktestunitintegration.repository.AccountRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = BankTestUnitIntegrationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteAccountIntegrationTest {

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
    public void deleteAccountWithSuccessTest() {

        accountFactory.create();

        RestAssured
                .given()
                .log().all()
                .when()
                .delete("/accounts/1")
                .then()
                .log().all()
                .statusCode(SC_NO_CONTENT);

        Assertions.assertEquals(0, accountRepository.count());
    }

    @Test
    public void deleteByIdNotFoundTest() {

        RestAssured
                .given()
                .log().all()
                .when()
                .delete("/accounts/1")
                .then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
