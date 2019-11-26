package com.invillia.banktestunitintegration.integrationTest;

import com.invillia.banktestunitintegration.BankTestUnitIntegrationApplication;
import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.request.AccountRequest;
import com.invillia.banktestunitintegration.factory.AccountRequestFactory;
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

import static javax.servlet.http.HttpServletResponse.SC_CREATED;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = BankTestUnitIntegrationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaveAccountIntegrationTest {

    @LocalServerPort
    private int portApi;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRequestFactory accountRequestFactory;

    @BeforeEach
    public void init() {
        RestAssured.port = portApi;
    }

    @Test
    public void shouldcreateAccountWithSuccessTest() {

        final AccountRequest accountRequest = accountRequestFactory.build();

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(accountRequest)
                .when()
                .post("/accounts")
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .header("Location", Matchers.endsWith("/accounts/1"));

        Assertions.assertEquals(1, accountRepository.count());

        final Account account = accountRepository.findById(1L).get();

        Assertions.assertAll("account assert",
                () -> Assertions.assertEquals(accountRequest.getAgency(), account.getAgency()),
                () -> Assertions.assertEquals(accountRequest.getNumberAccount(), account.getNumberAccount()),
                () -> Assertions.assertEquals(accountRequest.getIdCustomer(), account.getCustomer().getId()),
                () -> Assertions.assertEquals(accountRequest.getAccountTypeString(), account.getAccountTypeEnum().toString()));
    }
}
