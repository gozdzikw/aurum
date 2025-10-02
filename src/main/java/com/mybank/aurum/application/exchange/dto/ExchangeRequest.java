package com.mybank.aurum.application.exchange.dto;

import com.mybank.aurum.domain.account.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ExchangeRequest {
    private Long accountId;
    private Currency from;
    private Currency to;
    private BigDecimal amount;
}
