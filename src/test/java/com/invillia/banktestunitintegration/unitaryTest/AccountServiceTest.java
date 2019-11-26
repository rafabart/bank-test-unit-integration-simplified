package com.invillia.banktestunitintegration.unitaryTest;

import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.request.AccountRequest;
import com.invillia.banktestunitintegration.domain.request.DepositRequest;
import com.invillia.banktestunitintegration.domain.request.WithdrawRequest;
import com.invillia.banktestunitintegration.domain.response.AccountResponse;
import com.invillia.banktestunitintegration.exception.AccountLimitExceededException;
import com.invillia.banktestunitintegration.exception.AccountNotFoundException;
import com.invillia.banktestunitintegration.exception.CustomerNotFoundException;
import com.invillia.banktestunitintegration.exception.NotPositiveNumberException;
import com.invillia.banktestunitintegration.factory.AccountFactory;
import com.invillia.banktestunitintegration.factory.AccountRequestFactory;
import com.invillia.banktestunitintegration.repository.AccountRepository;
import com.invillia.banktestunitintegration.repository.CustomerRepository;
import com.invillia.banktestunitintegration.service.AccountService;
import org.junit.jupiter.api.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private AccountRequestFactory accountRequestFactory;


    @Test
    void shouldSaveAccountWithSuccessTest() {

        final AccountRequest accountRequest = accountRequestFactory.build();
        accountRequest.setIdCustomer(1L);

        final Account accountDatabaseFake = accountFactory.build();
        accountDatabaseFake.setId(1L);
        accountDatabaseFake.getCustomer().setId(1L);

        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(accountDatabaseFake.getCustomer()));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(accountDatabaseFake);

        Long id = accountService.save(accountRequest);
        Assertions.assertEquals(1L, id);

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(1)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(captor.capture());
    }

    @Test
    void shouldNotSaveAccountWithoutIdCustomerTest() {

        final AccountRequest accountRequest = accountRequestFactory.build();

        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            accountService.save(accountRequest);
        });

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(0)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(captor.capture());
    }

    @Test
    void shouldDepositInAccountWithSuccessTest() {

        final DepositRequest depositRequest = new DepositRequest(2000.00, 1L);

        final Account accountDatabaseFake = accountFactory.build();
        accountDatabaseFake.setId(1L);
        accountDatabaseFake.setCreatedAt(LocalDateTime.now());
        accountDatabaseFake.setUpdatedAt(LocalDateTime.now());

        final Account accountAfterDepositSuccess = accountDatabaseFake;
        accountAfterDepositSuccess.setBalance(accountDatabaseFake.getBalance() + depositRequest.getDeposit());

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(accountDatabaseFake));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(accountAfterDepositSuccess);

        AccountResponse accountResponse = accountService.deposit(depositRequest);

        Assertions.assertEquals(accountResponse.getBalance(), accountDatabaseFake.getBalance());

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(1)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountRepository, Mockito.times(1)).findById(captor.capture());
    }


    @Test
    void shouldDepositInAccountWithoutIdCustomerTest() {

        final DepositRequest depositRequest = new DepositRequest(2000.00, 1L);

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.deposit(depositRequest);
        });

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(0)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountRepository, Mockito.times(1)).findById(captor.capture());
    }


    @Test
    void shouldDepositInAccountWithNegativeNumber() {

        final DepositRequest depositRequest = new DepositRequest(-50.00, 1L);

        Assertions.assertThrows(NotPositiveNumberException.class, () -> {
            accountService.deposit(depositRequest);
        });

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(0)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountRepository, Mockito.times(0)).findById(captor.capture());
    }


    @Test
    void shouldWithdrawInAccountWithSuccessTest() {

        final WithdrawRequest withdrawRequest = new WithdrawRequest(500.00, 1L);

        final Account accountDatabaseFake = accountFactory.build();
        accountDatabaseFake.setId(1L);
        accountDatabaseFake.setCreatedAt(LocalDateTime.now());
        accountDatabaseFake.setUpdatedAt(LocalDateTime.now());

        final Account accountAfterWithdrawSuccess = accountDatabaseFake;
        accountAfterWithdrawSuccess.setBalance(accountDatabaseFake.getBalance() - withdrawRequest.getWithdraw());

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(accountDatabaseFake));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(accountAfterWithdrawSuccess);

        AccountResponse accountResponse = accountService.withdraw(withdrawRequest);

        Assertions.assertEquals(accountResponse.getBalance(), accountDatabaseFake.getBalance());

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(1)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountRepository, Mockito.times(1)).findById(captor.capture());
    }

    @Test
    void shouldWithdrawInAccountWithoutIdCustomerTest() {

        final WithdrawRequest withdrawRequest = new WithdrawRequest(500.00, 1L);

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.withdraw(withdrawRequest);
        });

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(0)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountRepository, Mockito.times(1)).findById(captor.capture());
    }


    @Test
    void shouldWithdrawInAccountWithoutLimitTest() {

        final WithdrawRequest withdrawRequest = new WithdrawRequest(2000.00, 1L);

        final Account accountDatabaseFake = accountFactory.build();
        accountDatabaseFake.setId(1L);
        accountDatabaseFake.setCreatedAt(LocalDateTime.now());
        accountDatabaseFake.setUpdatedAt(LocalDateTime.now());

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(accountDatabaseFake));

        Assertions.assertThrows(AccountLimitExceededException.class, () -> {
            accountService.withdraw(withdrawRequest);
        });

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(0)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountRepository, Mockito.times(1)).findById(captor.capture());
    }


    @Test
    void shouldWithdrawInAccountWithNegativeNumber() {

        final WithdrawRequest withdrawRequest = new WithdrawRequest(-10.00, 1L);

        Assertions.assertThrows(NotPositiveNumberException.class, () -> {
            accountService.withdraw(withdrawRequest);
        });

        final ArgumentCaptor<Account> captorAccount = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository, Mockito.times(0)).save(captorAccount.capture());

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountRepository, Mockito.times(0)).findById(captor.capture());
    }
}
