package com.invillia.banktestunitintegration.factory;

import br.com.leonardoferreira.jbacon.JBacon;
import com.invillia.banktestunitintegration.domain.enums.AccountTypeEnum;
import com.invillia.banktestunitintegration.domain.request.AccountRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountRequestFactory extends JBacon<AccountRequest> {

    private final CustomerFactory customerFactory;

    public AccountRequestFactory(CustomerFactory customerFactory) {
        this.customerFactory = customerFactory;
    }

    @Override
    protected AccountRequest getDefault() {

        return AccountRequest.builder()
                .numberAccount("123456")
                .idCustomer(customerFactory.create().getId())
                .balance(1000.00)
                .limitAccount(0.00)
                .agency("1111")
                .accountTypeString(AccountTypeEnum.SAVINGS.toString())
                .build();
    }

    @Override
    protected AccountRequest getEmpty() {
        return new AccountRequest();
    }

    @Override
    protected void persist(AccountRequest accountRequest) {
        throw new UnsupportedOperationException();
    }
}
