package com.invillia.banktestunitintegration.controller;

import com.invillia.banktestunitintegration.domain.request.CustomerRequest;
import com.invillia.banktestunitintegration.domain.response.CustomerResponse;
import com.invillia.banktestunitintegration.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public ResponseEntity find(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "cpf", required = false) String cpf
    ) {
        CustomerRequest customerRequestFilter = new CustomerRequest();
        customerRequestFilter.setName(name);
        customerRequestFilter.setCpf(cpf);


        List<CustomerResponse> customerResponseList = customerService.find(customerRequestFilter);
        return ResponseEntity.ok(customerResponseList);
    }


    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable final Long id) {
        CustomerResponse customerResponse = customerService.findById(id);
        return ResponseEntity.ok(customerResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity update(@Valid @PathVariable final Long id,
                                 @Valid @RequestBody final CustomerRequest customerRequest) {

        customerService.update(id, customerRequest);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity save(@Valid @RequestBody final CustomerRequest customerRequest) {

        Long idCustomer = customerService.save(customerRequest);

        final URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/customers/{id}")
                .build(idCustomer);

        return ResponseEntity.created(location).build();
    }
}
