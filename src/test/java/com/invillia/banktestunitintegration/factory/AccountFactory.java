package com.invillia.banktestunitintegration.factory;

import br.com.leonardoferreira.jbacon.JBacon;
import com.invillia.banktestunitintegration.domain.Account;
import com.invillia.banktestunitintegration.domain.Customer;
import com.invillia.banktestunitintegration.domain.enums.AccountTypeEnum;
import com.invillia.banktestunitintegration.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory extends JBacon<Account> {

    private final AccountRepository accountRepository;

    private final CustomerFactory customerFactory;

    public AccountFactory(AccountRepository accountRepository, CustomerFactory customerFactory) {
        this.accountRepository = accountRepository;
        this.customerFactory = customerFactory;
    }


    @Override
    protected Account getDefault() {

        final Account account = new Account();

        account.setNumberAccount("123456");
        account.setCustomer(customerFactory.create());
        account.setBalance(1000.00);
        account.setLimitAccount(200.00);
        account.setAgency("1111");
        account.setAccountTypeEnum(AccountTypeEnum.SAVINGS);
        return account;
    }

    @Override
    protected Account getEmpty() {
        return new Account();
    }

    @Override
    protected void persist(final Account account) {
        accountRepository.save(account);
    }
}
