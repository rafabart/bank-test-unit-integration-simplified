package com.invillia.banktestunitintegration.domain.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    @NotNull(message = "Nome não pode ser nulo!")
    @NotBlank(message = "Nome não pode estar em branco!")
    @Size(min = 14, max = 14, message = "CPF deve ter 14 caractéres")
    private String cpf;

    @NotNull(message = "Nome não pode ser nulo!")
    @NotBlank(message = "Nome não pode estar em branco!")
    private String name;
}
