package com.invillia.banktestunitintegration.service.impl;

import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.request.AccountRequest;
import com.invillia.banktestunitintegration.domain.request.DepositRequest;
import com.invillia.banktestunitintegration.domain.request.WithdrawRequest;
import com.invillia.banktestunitintegration.domain.response.AccountResponse;
import com.invillia.banktestunitintegration.exception.AccountLimitExceededException;
import com.invillia.banktestunitintegration.exception.AccountNotFoundException;
import com.invillia.banktestunitintegration.exception.CustomerNotFoundException;
import com.invillia.banktestunitintegration.exception.NotPositiveNumberException;
import com.invillia.banktestunitintegration.mapper.AccountMapper;
import com.invillia.banktestunitintegration.repository.AccountRepository;
import com.invillia.banktestunitintegration.repository.CustomerRepository;
import com.invillia.banktestunitintegration.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final AccountMapper accountMapper;


    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountMapper = accountMapper;
    }


    public AccountResponse deposit(DepositRequest depositRequest) {

        if (depositRequest.getDeposit() > 00.00) {

            final Account account = accountRepository.findById(depositRequest.getIdAccount()).orElseThrow(() -> new AccountNotFoundException(
                    "Conta de ID " + depositRequest.getIdAccount() + " não encontrada!"));

            account.setBalance(account.getBalance() + depositRequest.getDeposit());

            final Account accountSaved = accountRepository.save(account);

            //Retorno usado no teste unitário
            return accountMapper.accountToAccountResponse(accountSaved);

        } else {
            throw new NotPositiveNumberException("O Valor do deposito deve ser positivo!");
        }

    }


    public AccountResponse withdraw(WithdrawRequest withdrawRequest) {
        if (withdrawRequest.getWithdraw() > 00.00) {

            final Account account = accountRepository.findById(withdrawRequest.getIdAccount()).orElseThrow(() -> new AccountNotFoundException(
                    "Conta de ID " + withdrawRequest.getIdAccount() + " não encontrada!"));

            if (!((account.getBalance() - withdrawRequest.getWithdraw()) < -1 * account.getLimitAccount())) {
                account.setBalance(account.getBalance() - withdrawRequest.getWithdraw());
            } else {
                throw new AccountLimitExceededException(
                        "Limite de R$ " + account.getLimitAccount() + " excedido!");
            }

            final Account accountSaved = accountRepository.save(account);

            //Retorno usado no teste unitário
            return accountMapper.accountToAccountResponse(accountSaved);

        } else {
            throw new NotPositiveNumberException("O Valor do saque deve ser positivo!");
        }
    }


    @Transactional(readOnly = true)
    public List<AccountResponse> find(final AccountRequest accountRequestFilter) {

        Account accountFilter = accountMapper.accountRequestToAccount(accountRequestFilter);

        final Example exampleAccount = Example.of(accountFilter,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        final List<Account> accounts = accountRepository.findAll(exampleAccount);

        return accountMapper.accountToAccountResponse(accounts);
    }


    @Transactional(readOnly = true)
    public AccountResponse findById(final Long id) {
        final Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(
                "Conta de ID " + id + " não encontrada!"));

        return accountMapper.accountToAccountResponse(account);
    }


    public void update(final Long id, final AccountRequest accountRequest) {

        final Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(
                "Conta de ID " + id + " não encontrada!"));

        accountMapper.updateAccountByAccountRequest(account, accountRequest);

        account.setCustomer(customerRepository.findById(accountRequest.getIdCustomer()).orElseThrow(
                () -> new CustomerNotFoundException("Pessoa de ID não encontrada!")));

        accountRepository.save(account);
    }


    public void delete(final Long id) {
        final Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(
                "Pessoa de ID " + id + " não encontrada!"));

        accountRepository.delete(account);
    }


    public Long save(final AccountRequest accountRequest) {

        final Account account = accountMapper.accountRequestToAccount(accountRequest);

        account.setCustomer(customerRepository.findById(accountRequest.getIdCustomer()).orElseThrow(
                () -> new CustomerNotFoundException("Pessoa de ID não encontrada!")));

        final Account accountSaved = accountRepository.save(account);

        return accountSaved.getId();
    }
}
