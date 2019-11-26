package com.invillia.banktestunitintegration.domain.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @Positive(message = "O valor deve ser maior que zero!")
    @NotNull(message = "O valor do depósito não pode ser nulo!")
    private Double deposit;

    @NotNull(message = "O Id da conta não pode ser nulo!")
    private Long idAccount;
}
