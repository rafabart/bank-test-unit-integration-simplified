package com.invillia.banktestunitintegration.mapper;

import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.Customer;
import com.invillia.banktestunitintegration.domain.enums.AccountTypeEnum;
import com.invillia.banktestunitintegration.domain.request.AccountRequest;
import com.invillia.banktestunitintegration.domain.response.AccountResponse;
import com.invillia.banktestunitintegration.exception.AccountNotFoundException;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public AccountResponse accountToAccountResponse(final Account account) {

        return AccountResponse.builder()
                .id(account.getId())
                .numberAccount(account.getNumberAccount())
                .agency(account.getAgency())
                .balance(account.getBalance())
                .limitAccount(account.getLimitAccount())
                .accountTypeString(account.getAccountTypeEnum().toString())

                .customer(account.getCustomer())
//                .IdCustomer(account.getCustomer().getId())

                .createdAt(account.getCreatedAt().format(formatter))
                .updatedAt(account.getUpdatedAt().format(formatter))
                .build();
    }

    public List<AccountResponse> accountToAccountResponse(final List<Account> accounts) {

        return accounts.stream()
                .map(this::accountToAccountResponse)
                .collect(Collectors.toList());
    }

    public Account accountRequestToAccount(final AccountRequest accountRequest) {

        Account account = new Account();

        account.setNumberAccount(accountRequest.getNumberAccount());
        account.setAgency(accountRequest.getAgency());
        account.setBalance(accountRequest.getBalance());
        account.setLimitAccount(accountRequest.getLimitAccount());

        if (accountRequest.getIdCustomer() != null) {
            Customer customer = new Customer();
            customer.setId(accountRequest.getIdCustomer());
            account.setCustomer(customer);
        }

        if (accountRequest.getAccountTypeString() != null && !accountRequest.getAccountTypeString().isBlank()) {
            try {
                account.setAccountTypeEnum(AccountTypeEnum.valueOf(accountRequest.getAccountTypeString()));
            } catch (Exception e) {
                throw new AccountNotFoundException("Tipo de conta não cadastrado: " + e.getMessage());
            }
        }
        return account;
    }


    public void updateAccountByAccountRequest(final Account account, final AccountRequest accountRequest) {

        account.setNumberAccount(accountRequest.getNumberAccount());
        account.setAgency(accountRequest.getAgency());
        account.setBalance(accountRequest.getBalance());
        account.setLimitAccount(accountRequest.getLimitAccount());
        account.setAccountTypeEnum(AccountTypeEnum.valueOf(accountRequest.getAccountTypeString()));
    }
}
