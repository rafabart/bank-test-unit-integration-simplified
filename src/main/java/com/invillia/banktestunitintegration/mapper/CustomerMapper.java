package com.invillia.banktestunitintegration.mapper;

import com.invillia.banktestunitintegration.domain.Customer;
import com.invillia.banktestunitintegration.domain.request.CustomerRequest;
import com.invillia.banktestunitintegration.domain.response.CustomerResponse;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public CustomerResponse customerToCustomerResponse(final Customer customer) {

        return CustomerResponse.builder()
                .id(customer.getId())
                .cpf(customer.getCpf())
                .name(customer.getName())
                .accounts(customer.getAccounts())
                .createdAt(customer.getCreatedAt().format(formatter))
                .updatedAt(customer.getUpdatedAt().format(formatter))
                .build();
    }

    public List<CustomerResponse> customerToCustomerResponse(final List<Customer> customers) {

        return customers.stream()
                .map(this::customerToCustomerResponse)
                .collect(Collectors.toList());
    }

    public Customer customerRequestToCustomer(final CustomerRequest customerRequest) {

        Customer customer = new Customer();
        customer.setCpf(customerRequest.getCpf());
        customer.setName(customerRequest.getName());
        return customer;
    }

    public void updateCustomerByCustomerRequest(final Customer customer, final CustomerRequest customerRequest) {

        customer.setName(customerRequest.getName());
        customer.setCpf(customerRequest.getCpf());
    }
}
