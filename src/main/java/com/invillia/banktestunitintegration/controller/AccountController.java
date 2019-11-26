package com.invillia.banktestunitintegration.controller;


import com.invillia.banktestunitintegration.domain.request.AccountRequest;
import com.invillia.banktestunitintegration.domain.request.DepositRequest;
import com.invillia.banktestunitintegration.domain.request.WithdrawRequest;
import com.invillia.banktestunitintegration.domain.response.AccountResponse;
import com.invillia.banktestunitintegration.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/withdraw")
    public ResponseEntity withdraw(@Valid @RequestBody final WithdrawRequest withdrawRequest) {

        accountService.withdraw(withdrawRequest);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/deposit")
    public ResponseEntity deposit(@Valid @RequestBody final DepositRequest depositRequest) {

        accountService.deposit(depositRequest);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity find(
            @RequestParam(value = "numberAccount", required = false) String numberAccount,
            @RequestParam(value = "agency", required = false) String agency,
            @RequestParam(value = "balance", required = false) Double balance,
            @RequestParam(value = "limitAccount", required = false) Double limitAccount,
            @RequestParam(value = "accountTypeString", required = false) String accountTypeString,
            @RequestParam(value = "idCustomer", required = false) Long idCustomer
    ) {
        AccountRequest accountRequestFilter = new AccountRequest();
        accountRequestFilter.setNumberAccount(numberAccount);
        accountRequestFilter.setAgency(agency);
        accountRequestFilter.setBalance(balance);
        accountRequestFilter.setLimitAccount(limitAccount);
        accountRequestFilter.setAccountTypeString(accountTypeString);
        accountRequestFilter.setIdCustomer(idCustomer);

        List<AccountResponse> accountResponseList = accountService.find(accountRequestFilter);
        return ResponseEntity.ok(accountResponseList);
    }


    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable final Long id) {
        AccountResponse accountResponse = accountService.findById(id);
        return ResponseEntity.ok(accountResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity update(@Valid @PathVariable final Long id,
                                 @Valid @RequestBody final AccountRequest accountRequest) {

        accountService.update(id, accountRequest);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity save(@Valid @RequestBody final AccountRequest accountRequest) {

        Long idAccount = accountService.save(accountRequest);

        final URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/accounts/{id}")
                .build(idAccount);

        return ResponseEntity.created(location).build();
    }
}