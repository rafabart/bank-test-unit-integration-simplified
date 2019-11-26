package com.invillia.banktestunitintegration.service;

import com.invillia.banktestunitintegration.domain.request.CustomerRequest;
import com.invillia.banktestunitintegration.domain.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    public List<CustomerResponse> find(final CustomerRequest customerRequestFilter);

    public CustomerResponse findById(final Long id);

    public void update(final Long id, final CustomerRequest customerRequest);

    public void delete(final Long id);

    public Long save(final CustomerRequest customerRequest);

}
