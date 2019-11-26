package com.invillia.banktestunitintegration.factory;

import br.com.leonardoferreira.jbacon.JBacon;
import com.invillia.banktestunitintegration.domain.Customer;
import com.invillia.banktestunitintegration.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerFactory extends JBacon<Customer> {

    private final CustomerRepository customerRepository;

    public CustomerFactory(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    @Override
    protected Customer getDefault() {
        final Customer customer = new Customer();

        customer.setName("Rafael Marinho");
        customer.setCpf("123.123.123-12");

        return customer;
    }

    @Override
    protected Customer getEmpty() {
        return new Customer();
    }

    @Override
    protected void persist(Customer customer) {
        customerRepository.save(customer);
    }
}
