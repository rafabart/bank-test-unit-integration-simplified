package com.invillia.banktestunitintegration.domain.response;

import com.invillia.banktestunitintegration.domain.Account;
import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Long id;

    private String cpf;

    private String name;

    private List<Account> accounts;

    private String createdAt;

    private String updatedAt;
}
