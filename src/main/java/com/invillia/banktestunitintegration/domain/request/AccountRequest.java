package com.invillia.banktestunitintegration.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    @Size(min = 6, max = 6, message = "Número da conta deve ter 6 caractéres")
    @NotNull(message = "Número da conta não pode ser nulo!")
    @NotBlank(message = "Número da conta não pode estar em branco!")
    private String numberAccount;

    @Size(min = 4, max = 4, message = "Agencia deve ter 4 caractéres")
    @NotNull(message = "Agencia não pode ser nulo!")
    @NotBlank(message = "Agencia não pode estar em branco!")
    private String agency;

    @NotNull(message = "Saldo não pode ser nulo!")
    private Double balance;

    @NotNull(message = "Limite não pode ser nulo!")
    private Double limitAccount;

    @NotNull(message = "Tipo de conta não pode ser nulo!")
    @NotBlank(message = "Tipo de conta não pode estar em branco!")
    private String accountTypeString;

    @NotNull(message = "Id da pessoa não pode ser nulo!")
    private Long idCustomer;
}
