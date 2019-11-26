package com.invillia.banktestunitintegration.domain.response;

import com.invillia.banktestunitintegration.domain.Customer;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private Long id;

    private String numberAccount;

    private String agency;

    private Double balance;

    private Double limitAccount;

    private String accountTypeString;

    private Customer customer;

    private String createdAt;

    private String updatedAt;
}
