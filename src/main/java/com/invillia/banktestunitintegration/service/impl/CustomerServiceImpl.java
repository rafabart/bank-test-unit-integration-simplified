package com.invillia.banktestunitintegration.service.impl;

import com.invillia.banktestunitintegration.domain.Customer;
import com.invillia.banktestunitintegration.domain.request.CustomerRequest;
import com.invillia.banktestunitintegration.domain.response.CustomerResponse;
import com.invillia.banktestunitintegration.exception.CustomerNotFoundException;
import com.invillia.banktestunitintegration.mapper.CustomerMapper;
import com.invillia.banktestunitintegration.repository.AccountRepository;
import com.invillia.banktestunitintegration.repository.CustomerRepository;
import com.invillia.banktestunitintegration.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, AccountRepository accountRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.customerMapper = customerMapper;
    }


    @Transactional(readOnly = true)
    public List<CustomerResponse> find(final CustomerRequest customerRequestFilter) {

        Customer customerFilter = customerMapper.customerRequestToCustomer(customerRequestFilter);

        final Example exampleCustomer = Example.of(customerFilter,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        final List<Customer> customers = customerRepository.findAll(exampleCustomer);

        return customerMapper.customerToCustomerResponse(customers);
    }


    @Transactional(readOnly = true)
    public CustomerResponse findById(final Long id) {
        final Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(
                "Pessoa de ID " + id + " não encontrada!"));

        return customerMapper.customerToCustomerResponse(customer);
    }


    public void update(final Long id, final CustomerRequest customerRequest) {

        final Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(
                "Pessoa de ID " + id + " não encontrada!"));

        customerMapper.updateCustomerByCustomerRequest(customer, customerRequest);

        customerRepository.save(customer);
    }


    public void delete(final Long id) {
        final Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(
                "Pessoa de ID " + id + " não encontrada!"));

        customerRepository.delete(customer);
    }


    public Long save(final CustomerRequest customerRequest) {

        final Customer customer = customerMapper.customerRequestToCustomer(customerRequest);

        final Customer customerSaved = customerRepository.save(customer);

        return customerSaved.getId();
    }
}
