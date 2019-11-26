package com.invillia.banktestunitintegration.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.invillia.banktestunitintegration.domain.enums.AccountTypeEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Account extends IdAbstract<Long> {

    @Column(nullable = false, length = 6)
    private String numberAccount;

    @Column(nullable = false, length = 4)
    private String agency;

    @Column(nullable = false, precision = 10, scale = 2, columnDefinition = "double default 0.00")
    private Double balance;

    @PositiveOrZero(message = "Limite não pode ser negativo!")
    @Column(nullable = false, precision = 10, scale = 2, columnDefinition = "double default 0.00")
    private Double limitAccount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountTypeEnum;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;
}