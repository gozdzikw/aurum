package com.mybank.aurum.domain.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountCreateRequest {

    @NotBlank(message = "Please, insert a first name.")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Please, insert a last name.")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull(message = "Please, provide balance.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance should be greater or equal 0.")
    private BigDecimal initialBalance;

    public AccountCreateRequest(String firstName, String lastName, BigDecimal initialBalance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.initialBalance = initialBalance;
    }
}
